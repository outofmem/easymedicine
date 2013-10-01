/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.outofmemory.easymedicine.model.Distributor;
import com.outofmemory.easymedicine.model.MedicineStore;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * The data access logic of distributors collection
 * 
 * @author pribiswas
 * 
 */
@Repository
public class DistributorDao extends AbstractBaseDao {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DistributorDao.class);
	private final DBCollection distributorCollection;

	public DistributorDao() {
		distributorCollection = easyMedicineDatabase
				.getCollection(MongoConstants.DISTRIBUTOR_COLLECTION);
	}

	/**
	 * Add the given distributor in distributor collection
	 * 
	 * @param distributor
	 */
	public void addDistributor(Distributor distributor) {
		BasicDBObject distributorObj = getBasicDistributorObject(distributor);
		distributorCollection.insert(distributorObj);
		LOGGER.debug(distributor.getEmailAddress()
				+ " is successfully registered with its store");
	}

	/**
	 * Find a distributor by e-mail id
	 * 
	 * @param emailId
	 *            The e-mail id of the distributor
	 * @return An instance of {@link Distributor}
	 */
	public Distributor findDistributorByEmailId(String emailId) {
		Distributor distributor = null;
		DBObject query = QueryBuilder.start(MongoConstants.KEY_ID).is(emailId)
				.get();
		DBObject distributorObj = distributorCollection.findOne(query);
		if (distributorObj != null) {
			distributor = getDistributor(distributorObj);
		}
		return distributor;
	}

	/**
	 * Checks whether given e-mail id is already registered or not
	 * 
	 * @param emailId
	 *            The e-mail id
	 * @return true if the e-mail id is already registered, false otherwise
	 */
	public boolean doesEmailExist(String emailId) {
		DBObject query = QueryBuilder.start(MongoConstants.KEY_ID).is(emailId)
				.get();
		DBObject projection = new BasicDBObject(MongoConstants.KEY_ID, true);
		return distributorCollection.findOne(query, projection) != null;
	}

	/**
	 * Checks whether given mobile is already registered or not
	 * 
	 * @param mobile
	 *            The mobile number
	 * @return true if the mobile is already registered, false otherwise
	 */
	public boolean doesMobileExist(String mobile) {
		DBObject query = QueryBuilder.start(MongoConstants.KEY_MOBILE)
				.is(mobile).get();
		DBObject projection = new BasicDBObject(MongoConstants.KEY_ID, true);
		return distributorCollection.findOne(query, projection) != null;
	}

	/**
	 * Check whether the given store exist or not
	 * 
	 * @param store
	 *            The example reference of medicine store
	 * @return true if given store exist, false otherwise
	 */
	public boolean doesStoreExist(MedicineStore store) {
		DBObject name = new BasicDBObject(new StringBuilder(
				MongoConstants.KEY_STORE).append(".")
				.append(MongoConstants.KEY_NAME).toString(), store.getName());
		DBObject locality = new BasicDBObject(new StringBuilder(
				MongoConstants.KEY_STORE).append(".")
				.append(MongoConstants.KEY_LOCALITY).toString(),
				store.getLocality());
		DBObject city = new BasicDBObject(new StringBuilder(
				MongoConstants.KEY_STORE).append(".")
				.append(MongoConstants.KEY_CITY).toString(), store.getCity());
		DBObject pin = new BasicDBObject(new StringBuilder(
				MongoConstants.KEY_STORE).append(".")
				.append(MongoConstants.KEY_PIN).toString(), store.getPin());

		DBObject query = QueryBuilder.start().and(name, locality, city, pin)
				.get();
		DBObject projection = new BasicDBObject(MongoConstants.KEY_ID, true);
		return distributorCollection.findOne(query, projection) != null;
	}

	/**
	 * Get the hashed and salted password of the distributor login
	 * 
	 * @param emailId
	 *            The registered e-mail id of the distributor
	 * @return null if distributor is not present, hashed and salted password
	 *         otherwise
	 */
	public String getDistributorPassword(String emailId) {
		DBObject distributor = distributorCollection.findOne(new BasicDBObject(
				MongoConstants.KEY_ID, emailId));
		if (distributor == null) {
			return null;
		}
		return distributor.get(MongoConstants.KEY_PASSWORD).toString();
	}

	/**
	 * Update the distributor's password
	 * 
	 * @param emailId
	 *            The e-mail id of the distributor
	 * @param newPassword
	 *            The new password
	 * @return true if update is successful, false otherwise
	 */
	public boolean updateDistributorPassword(String emailId, String newPassword) {
		distributorCollection.update(new BasicDBObject(MongoConstants.KEY_ID,
				emailId), new BasicDBObject(MongoConstants.OPERATOR_SET,
				new BasicDBObject(MongoConstants.KEY_PASSWORD, newPassword)));
		return true;
	}

	/**
	 * Generate an instance of {@link Distributor} from result {@link DBObject}
	 * 
	 * @param distributorObj
	 * @return an instance of {@link Distributor}
	 */
	private Distributor getDistributor(DBObject distributorObj) {
		Distributor distributor = new Distributor();
		distributor.setEmailAddress(distributorObj.get(MongoConstants.KEY_ID)
				.toString());
		distributor.setMobileNumber(distributorObj.get(
				MongoConstants.KEY_MOBILE).toString());

		DBObject storeObj = (DBObject) distributorObj
				.get(MongoConstants.KEY_STORE);
		MedicineStore medicineStore = new MedicineStore();
		medicineStore.setName(storeObj.get(MongoConstants.KEY_NAME).toString());
		medicineStore.setAddress(storeObj.get(MongoConstants.KEY_ADDRESS)
				.toString());
		medicineStore.setLocality(storeObj.get(MongoConstants.KEY_LOCALITY)
				.toString());
		medicineStore.setCity(storeObj.get(MongoConstants.KEY_CITY).toString());
		medicineStore.setPin(storeObj.get(MongoConstants.KEY_PIN).toString());

		distributor.setMedicineStore(medicineStore);

		return distributor;
	}

	/**
	 * Get the basic database object
	 * 
	 * @param distributor
	 *            An instance of {@link Distributor}
	 * @return An instance of {@link BasicDBObject}
	 */
	private BasicDBObject getBasicDistributorObject(Distributor distributor) {
		BasicDBObject distributorObj = new BasicDBObject();
		distributorObj
				.append(MongoConstants.KEY_ID, distributor.getEmailAddress())
				.append(MongoConstants.KEY_PASSWORD, distributor.getPassword())
				.append(MongoConstants.KEY_MOBILE,
						distributor.getMobileNumber());

		MedicineStore medicineStore = distributor.getMedicineStore();
		BasicDBObject storeObj = new BasicDBObject();
		storeObj.append(MongoConstants.KEY_NAME, medicineStore.getName());
		storeObj.append(MongoConstants.KEY_ADDRESS, medicineStore.getAddress());
		storeObj.append(MongoConstants.KEY_LOCALITY,
				medicineStore.getLocality());
		storeObj.append(MongoConstants.KEY_CITY, medicineStore.getCity());
		storeObj.append(MongoConstants.KEY_PIN, medicineStore.getPin());

		distributorObj.append(MongoConstants.KEY_STORE, storeObj);
		return distributorObj;
	}

}
