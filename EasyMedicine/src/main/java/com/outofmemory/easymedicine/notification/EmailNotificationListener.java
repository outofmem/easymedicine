/**
 * 
 */
package com.outofmemory.easymedicine.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * The listener class to send e-mail as notification.
 * 
 * @author pribiswas
 * 
 */
@Component
public class EmailNotificationListener extends NotificationListener {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EmailNotificationListener.class);

	@Autowired
	private MailSender emailSender;
	@Autowired
	private SimpleMailMessage templateEmailMessage;

	/**
	 * @see com.outofmemory.easymedicine.notification.NotificationListener#sendNotification(Notification)
	 */
	@Override
	public void sendNotification(Notification notification) {
		SimpleMailMessage emailMessage = new SimpleMailMessage(
				templateEmailMessage);
		emailMessage.setTo(notification.getEmailAddress());
		emailMessage.setSubject(notification.getSubject());
		emailMessage.setText(notification.getMessage());

		try {
			emailSender.send(emailMessage);
			LOGGER.debug("E-mail sent to :" + notification.getEmailAddress()
					+ " with subject :" + notification.getSubject());
		} catch (MailException e) {
			LOGGER.error("Could not send mail to "
					+ notification.getEmailAddress());
			e.printStackTrace();
		}
	}

}
