/**
 * 
 */
package com.outofmemory.easymedicine.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.outofmemory.easymedicine.dao.DistributorDao;
import com.outofmemory.easymedicine.form.DistributorRegistrationForm;
import com.outofmemory.easymedicine.model.Distributor;
import com.outofmemory.easymedicine.model.MedicineStore;
import com.outofmemory.easymedicine.notification.Notification;
import com.outofmemory.easymedicine.notification.NotificationListener;

/**
 * The business logic of all the distributor related operations like login,
 * registration, get order notification are written in this class.
 * 
 * @author pribiswas
 * 
 */
@Service
public class DistributorService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DistributorService.class);

	@Autowired
	private DistributorDao distributorDao;
	private final Random random = new SecureRandom();
	@Autowired
	private Collection<NotificationListener> notificationListeners;
	@Autowired
	private MessageSource messageSource;

	/**
	 * Add a distributor into the system
	 * 
	 * @param form
	 *            The form filled up by the distributor
	 * @return true if adding is successful, false otherwise
	 */
	public boolean addDistributor(DistributorRegistrationForm form) {
		Distributor distributor = getDistributorFromForm(form);
		distributorDao.addDistributor(distributor);
		LOGGER.info("Following distributor is registered succesfully : "
				+ distributor);
		// we need to send a registration notification
		Notification regitrationNotification = new Notification(
				distributor.getEmailAddress(), distributor.getMobileNumber(),
				messageSource.getMessage(
						"distributor.registration.notification.subject",
						new Object[0], Locale.ENGLISH),
				messageSource.getMessage(
						"distributor.registration.notification.message",
						new Object[0], Locale.ENGLISH));
		sendNotification(regitrationNotification);
		return true;
	}

	/**
	 * Check whether e-mail is already registered or not
	 * 
	 * @param emailId
	 * @return true if e-mail is already registered with system, false otherwise
	 */
	public boolean isEmailAlreadyRegistered(String emailId) {
		return distributorDao.doesEmailExist(emailId);
	}

	/**
	 * Check whether mobile is already registered or not
	 * 
	 * @param mobile
	 * @return true if mobile is already registered with system, false otherwise
	 */
	public boolean isMobileAlreadyRegistered(String mobile) {
		return distributorDao.doesMobileExist(mobile);
	}

	/**
	 * Check whether the given store information is already registered or not
	 * 
	 * @param name
	 * @param locality
	 * @param city
	 * @param pin
	 * @return true if store is already registered with system, false otherwise
	 */
	public boolean isStoreAlreadyRegistered(String name, String locality,
			String city, String pin) {
		MedicineStore store = new MedicineStore();
		store.setName(name);
		store.setLocality(locality);
		store.setCity(city);
		store.setPin(pin);

		return distributorDao.doesStoreExist(store);
	}

	/**
	 * Authenticate the login credential
	 * 
	 * @param emailId
	 *            The e-mail id of the distributor
	 * @param password
	 *            The password entered by user
	 * @return true if authentication is successful, false otherwise
	 */
	public boolean authenticateDistributor(String emailId, String password) {
		String originalPassword = distributorDao
				.getDistributorPassword(emailId);
		if (originalPassword == null) {
			LOGGER.debug(emailId + " is not registered with us.");
			return false;
		}
		String salt = originalPassword.split(",")[1];
		return originalPassword.equals(makeHashedAndSaltedPassword(password,
				salt));
	}

	/**
	 * Reset the password for given emailId and notify the new password to
	 * distributor
	 * 
	 * @param emailId
	 */
	public void resetDistributorPassword(String emailId) {
		Distributor distributor = distributorDao
				.findDistributorByEmailId(emailId);
		if (distributor != null) {
			String newPassword = RandomStringUtils.randomAlphanumeric(10);
			boolean passwordUpdated = distributorDao.updateDistributorPassword(
					emailId,
					makeHashedAndSaltedPassword(newPassword,
							Integer.toString(random.nextInt())));
			if (passwordUpdated) {
				LOGGER.debug("Password is reset for " + emailId);
				// Send reset password notification
				Notification passwordResetNotification = new Notification(
						distributor.getEmailAddress(),
						distributor.getMobileNumber(),
						messageSource
								.getMessage(
										"distributor.password.reset.notification.subject",
										new Object[0], Locale.ENGLISH),
						messageSource
								.getMessage(
										"distributor.password.reset.notification.message",
										new Object[] { newPassword },
										Locale.ENGLISH));
				sendNotification(passwordResetNotification);
			}
		}
	}

	/**
	 * Update the given password for given e-mail id and notify the event to
	 * distributor
	 * 
	 * @param emailId
	 * @param newPassword
	 */
	public void changeDistributorPassword(String emailId, String newPassword) {
		Distributor distributor = distributorDao
				.findDistributorByEmailId(emailId);
		if (distributor != null) {
			boolean passwordUpdated = distributorDao.updateDistributorPassword(
					emailId,
					makeHashedAndSaltedPassword(newPassword,
							Integer.toString(random.nextInt())));
			if (passwordUpdated) {
				LOGGER.debug("Password is changed for " + emailId);
				// Send the password change notification
				Notification passwordChangeNotification = new Notification(
						distributor.getEmailAddress(),
						distributor.getMobileNumber(),
						messageSource
								.getMessage(
										"distributor.password.change.notification.subject",
										new Object[0], Locale.ENGLISH),
						messageSource
								.getMessage(
										"distributor.password.change.notification.message",
										new Object[0], Locale.ENGLISH));
				sendNotification(passwordChangeNotification);
			}
		}
	}

	/**
	 * Create an instance of {@link Distributor} from submitted form
	 * 
	 * @param form
	 *            The submitted form
	 * @return An instance of {@link Distributor}
	 */
	private Distributor getDistributorFromForm(DistributorRegistrationForm form) {
		Distributor distributor = new Distributor();
		distributor.setEmailAddress(form.getEmailAddress());
		distributor.setPassword(makeHashedAndSaltedPassword(form.getPassword(),
				Integer.toString(random.nextInt())));
		distributor.setMobileNumber(form.getMobileNumber());

		MedicineStore medicineStore = new MedicineStore();
		medicineStore.setName(form.getMedicineStoreName());
		medicineStore.setAddress(form.getMedicineStoreAddress());
		medicineStore.setLocality(form.getMedicineStoreLocality());
		medicineStore.setCity(form.getMedicineStoreCity());
		medicineStore.setPin(form.getMedicineStorePin());

		distributor.setMedicineStore(medicineStore);
		return distributor;
	}

	/**
	 * Generate an encrypted form of given password
	 * 
	 * @param password
	 *            The string given by user
	 * @param salt
	 *            The salt which is used to encrypt the given password
	 * @return A encrypted form of password
	 */
	private String makeHashedAndSaltedPassword(String password, String salt) {
		try {
			String saltedPassword = new StringBuilder(password).append(",")
					.append(salt).toString();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(saltedPassword.getBytes());
			byte[] hashedAndSaltedBytes = new String(messageDigest.digest(),
					"UTF-8").getBytes();
			return new StringBuilder(new String(
					Base64.encodeBase64(hashedAndSaltedBytes))).append(",")
					.append(salt).toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(
					"MD5 algorithm is not available to digest the password.");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UTF-8 encoding is not available to encode the password.");
		}
	}

	/**
	 * Send the given notification to listeners
	 * 
	 * @param notification
	 */
	private void sendNotification(Notification notification) {
		for (NotificationListener listener : notificationListeners) {
			listener.notify(notification);
		}
	}
}
