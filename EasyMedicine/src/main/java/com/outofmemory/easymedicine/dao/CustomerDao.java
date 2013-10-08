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
import com.outofmemory.easymedicine.model.Address;
import com.outofmemory.easymedicine.model.Customer;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * The data access logic of the customer collection
 * 
 * @author pribiswas
 * 
 */
@Repository
public class CustomerDao extends AbstractBaseDao {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerDao.class);
	private final DBCollection customerCollection;

	public CustomerDao() {
		customerCollection = easyMedicineDatabase
				.getCollection(MongoConstants.CUSTOMER_COLLECTION);
	}

	/**
	 * Find a customer by his/her e-mail id
	 * 
	 * @param emailId
	 *            The e-mail id of the customer
	 * @return An instance of {@link Customer} if found, null otherwise
	 */
	public Customer findCustomerByEmailId(String emailId) {
		Customer customer = null;
		DBObject query = QueryBuilder.start(MongoConstants.KEY_ID).is(emailId)
				.get();
		DBObject customerObj = customerCollection.findOne(query);
		if (customerObj != null) {
			customer = new Customer();
			customer.setName(customerObj.get(MongoConstants.KEY_NAME)
					.toString());
			customer.setEmailId(customerObj.get(MongoConstants.KEY_ID)
					.toString());
			customer.setMobileNumber(customerObj.get(MongoConstants.KEY_MOBILE)
					.toString());
			Address address = new Address();
			DBObject addressObj = (DBObject) customerObj
					.get(MongoConstants.KEY_ADDRESS);
			address.setHouseOrFlatNumber(addressObj.get(
					MongoConstants.KEY_HOUSE_OR_FLAT_NUMBER).toString());
			address.setStreetAddress(addressObj.get(
					MongoConstants.KEY_STREET_ADDRESS).toString());
			address.setLocality(addressObj.get(MongoConstants.KEY_LOCALITY)
					.toString());
			address.setCity(addressObj.get(MongoConstants.KEY_CITY).toString());
			address.setPin(addressObj.get(MongoConstants.KEY_PIN).toString());
			customer.setAddress(address);
		}
		return customer;
	}

	/**
	 * Add the specified customer into the customer collection, if it is not
	 * exist. Otherwise update the customer record.
	 * 
	 * @param customer
	 *            An instance of {@link Customer}
	 */
	public void addOrUpdateCustomer(Customer customer) {
		DBObject query = QueryBuilder.start(MongoConstants.KEY_ID)
				.is(customer.getEmailId()).get();
		BasicDBObject customerObj = new BasicDBObject();
		customerObj.append(MongoConstants.KEY_ID, customer.getEmailId())
				.append(MongoConstants.KEY_NAME, customer.getName())
				.append(MongoConstants.KEY_MOBILE, customer.getMobileNumber());

		Address address = customer.getAddress();
		BasicDBObject addressObj = new BasicDBObject();
		addressObj
				.append(MongoConstants.KEY_HOUSE_OR_FLAT_NUMBER,
						address.getHouseOrFlatNumber())
				.append(MongoConstants.KEY_STREET_ADDRESS,
						address.getStreetAddress())
				.append(MongoConstants.KEY_LOCALITY, address.getLocality())
				.append(MongoConstants.KEY_CITY, address.getCity())
				.append(MongoConstants.KEY_PIN, address.getPin());
		customerObj.append(MongoConstants.KEY_ADDRESS, addressObj);

		customerCollection.update(query, customerObj, true, false);
		LOGGER.debug(customer + " added/updated successfully.");
	}
}
