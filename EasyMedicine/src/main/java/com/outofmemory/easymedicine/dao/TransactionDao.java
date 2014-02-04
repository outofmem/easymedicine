/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
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

	/**
	 * Get a list of {@link OrderTransaction} by order id and sorted by cost in
	 * given order
	 * 
	 * @param orderId
	 *            The order reference number
	 * @param ascending
	 *            A flag to indicate whether sorting has been done in ascending
	 *            or descending order
	 * @return A list of {@link OrderTransaction}
	 */
	public List<OrderTransaction> getTransactionsByOrderIdSortedByCost(
			String orderId, boolean ascending) {
		List<OrderTransaction> transactions = new ArrayList<OrderTransaction>();
		DBObject query = QueryBuilder.start(MongoConstants.KEY_ORDER_ID)
				.is(orderId).get();
		DBObject orderBy = ascending ? new BasicDBObject(
				MongoConstants.KEY_COST, 1) : new BasicDBObject(
				MongoConstants.KEY_COST, -1);
		DBCursor cursor = transactionCollection.find(query).sort(orderBy);
		for (DBObject transactionObj : cursor) {
			OrderTransaction transaction = getOrderTransaction(transactionObj);
			transactions.add(transaction);
		}
		return transactions;
	}

	/**
	 * Get a list of order id by distributor id. The list contains only those
	 * orders for which cost is not set
	 * 
	 * @param distributorId
	 *            The id of the distributor
	 * @return A list of order id
	 */
	public List<String> getOrdersByDistributorId(String distributorId) {
		List<String> orders = new ArrayList<String>();
		DBObject query = QueryBuilder.start(MongoConstants.KEY_DISTRIBUTOR_ID)
				.is(distributorId).and(MongoConstants.KEY_COST)
				.is(new BasicDBObject(MongoConstants.OPERATOR_EXISTS, false))
				.get();
		return orders;
	}

	/**
	 * Remove the given transactions from the collection
	 * 
	 * @param transactions
	 *            An array of transactions need to be removed
	 */
	public void removeTransactions(OrderTransaction... transactions) {
		List<ObjectId> removeObjIds = new ArrayList<ObjectId>();
		for (OrderTransaction transaction : transactions) {
			removeObjIds.add(transaction.getId());
		}
		BasicDBObject query = new BasicDBObject(MongoConstants.KEY_ID,
				new BasicDBObject(MongoConstants.OPERATOR_IN, removeObjIds));
		transactionCollection.remove(query);
	}

	/**
	 * Get an instance of {@link OrderTransaction}
	 * 
	 * @param transactionObj
	 *            An instance of {@link DBObject}
	 * @return An instance of {@link OrderTransaction}
	 */
	private OrderTransaction getOrderTransaction(DBObject transactionObj) {
		OrderTransaction transaction = new OrderTransaction();
		transaction.setId(new ObjectId(transactionObj
				.get(MongoConstants.KEY_ID).toString()));
		transaction.setOrderId(transactionObj.get(MongoConstants.KEY_ORDER_ID)
				.toString());
		transaction.setCustomerId(transactionObj.get(
				MongoConstants.KEY_CUSTOMER_ID).toString());
		transaction.setDistributorId(transactionObj.get(
				MongoConstants.KEY_DISTRIBUTOR_ID).toString());
		if (transactionObj.get(MongoConstants.KEY_COST) != null) {
			transaction.setCost(new Double(transactionObj.get(
					MongoConstants.KEY_COST).toString()));
		}
		return transaction;
	}

}
