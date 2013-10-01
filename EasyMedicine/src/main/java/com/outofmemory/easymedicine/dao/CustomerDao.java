/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCollection;
import com.outofmemory.easymedicine.model.Customer;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * The data access logic of the customers collections
 * 
 * @author pribiswas
 * 
 */
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
		return null;
	}

	/**
	 * Add the specified customer into the customer collection, if it is not
	 * exist. Otherwise update the customer record.
	 * 
	 * @param customer
	 *            An instance of {@link Customer}
	 */
	public void addOrUpdateCustomer(Customer customer) {

	}

}
