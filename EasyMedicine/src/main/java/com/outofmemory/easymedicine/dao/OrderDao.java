/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.outofmemory.easymedicine.model.Medicine;
import com.outofmemory.easymedicine.model.Order;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * The data access logic of the order collection
 * 
 * @author pribiswas
 * 
 */
@Repository
public class OrderDao extends AbstractBaseDao {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderDao.class);

	private final DBCollection orderCollection;

	public OrderDao() {
		orderCollection = easyMedicineDatabase
				.getCollection(MongoConstants.ORDER_COLLECTION);
	}

	/**
	 * Add an order to order collection
	 * 
	 * @param order
	 *            An instance of {@link Order}
	 */
	public void addOrder(Order order) {
		BasicDBObject orderObj = new BasicDBObject();
		orderObj.append(MongoConstants.KEY_ID, order.getId())
				.append(MongoConstants.KEY_CUSTOMER_ID, order.getCustomerId())
				.append(MongoConstants.KEY_PATIENT, order.getPatient())
				.append(MongoConstants.KEY_DOCTOR, order.getDoctor());
		List<Medicine> medicines = order.getMedicines();
		BasicDBList medicinesObj = new BasicDBList();
		for (Medicine medicine : medicines) {
			BasicDBObject medicineObj = new BasicDBObject(
					MongoConstants.KEY_NAME, medicine.getName()).append(
					MongoConstants.KEY_POWER, medicine.getPower()).append(
					MongoConstants.KEY_QUANTITY, medicine.getQuantity());
			medicinesObj.add(medicineObj);
		}
		orderObj.append(MongoConstants.KEY_MEDICINES, medicinesObj);

		orderCollection.insert(orderObj);

		LOGGER.debug(order + " is inserted in order collection");
	}

	/**
	 * Find the order by order reference number
	 * 
	 * @param orderId
	 *            The order reference number
	 * @return An instance of {@link Order}
	 */
	public Order findOrderById(String orderId) {
		Order order = null;
		DBObject orderObj = orderCollection.findOne(new BasicDBObject(
				MongoConstants.KEY_ID, orderId));
		if (orderObj != null) {
			order = getOrder(orderObj);
		}
		return order;
	}

	/**
	 * Get the {@link Order} from an instance of order {@link DBObject}
	 * 
	 * @param orderObj
	 * @return An instance of {@link Order}
	 */
	private Order getOrder(DBObject orderObj) {
		Order order = new Order();
		order.setId(orderObj.get(MongoConstants.KEY_ID).toString());
		order.setCustomerId(orderObj.get(MongoConstants.KEY_CUSTOMER_ID)
				.toString());
		order.setPatient(orderObj.get(MongoConstants.KEY_PATIENT).toString());
		order.setDoctor(orderObj.get(MongoConstants.KEY_DOCTOR).toString());
		BasicDBList medicines = (BasicDBList) orderObj
				.get(MongoConstants.KEY_MEDICINES);
		for (Object object : medicines) {
			DBObject medicineObj = (DBObject) object;
			Medicine medicine = new Medicine();
			medicine.setName(medicineObj.get(MongoConstants.KEY_NAME)
					.toString());
			medicine.setPower(medicineObj.get(MongoConstants.KEY_POWER)
					.toString());
			medicine.setQuantity(Integer.parseInt(medicineObj.get(
					MongoConstants.KEY_QUANTITY).toString()));
			order.addMedicine(medicine);
		}
		return order;
	}

}
