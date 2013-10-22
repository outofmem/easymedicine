/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.outofmemory.easymedicine.model.OrderTransaction;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * The data access logic of the transaction collection
 * 
 * @author pribiswas
 * 
 */
@Repository
public class TransactionDao extends AbstractBaseDao {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransactionDao.class);

	private final DBCollection transactionCollection;

	public TransactionDao() {
		transactionCollection = easyMedicineDatabase
				.getCollection(MongoConstants.TRANSACTION_COLLECTION);
	}

	/**
	 * Add an transaction into the collection
	 * 
	 * @param transaction
	 */
	public void addOrderTransaction(OrderTransaction transaction) {
		BasicDBObject transactionObj = new BasicDBObject()
				.append(MongoConstants.KEY_ORDER_ID, transaction.getOrderId())
				.append(MongoConstants.KEY_CUSTOMER_ID,
						transaction.getCustomerId())
				.append(MongoConstants.KEY_DISTRIBUTOR_ID,
						transaction.getDistributorId());
		transactionCollection.insert(transactionObj);
	}

}
