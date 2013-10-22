/**
 * 
 */
package com.outofmemory.easymedicine.service;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.outofmemory.easymedicine.dao.CustomerDao;
import com.outofmemory.easymedicine.dao.DistributorDao;
import com.outofmemory.easymedicine.dao.OrderDao;
import com.outofmemory.easymedicine.dao.TransactionDao;
import com.outofmemory.easymedicine.form.OrderMedicineForm;
import com.outofmemory.easymedicine.model.Address;
import com.outofmemory.easymedicine.model.Customer;
import com.outofmemory.easymedicine.model.Distributor;
import com.outofmemory.easymedicine.model.Medicine;
import com.outofmemory.easymedicine.model.Order;
import com.outofmemory.easymedicine.model.OrderTransaction;

/**
 * The business logic of all the order related operations are written in this
 * class
 * 
 * @author pribiswas
 * 
 */
@Service
public class OrderService extends NotificationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderService.class);

	@Autowired
	private DistributorDao distributorDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private MessageSource messageSource;

	/**
	 * It search for nearby medicine stores who can serve the order. If found,
	 * it places the order for bidding.
	 * 
	 * @param form
	 *            An instance of {@link OrderMedicineForm}
	 * @return The order reference number, null if order can't be placed
	 */
	public String placeOrder(OrderMedicineForm form) {
		String orderId = null;
		List<Distributor> nearByDistributors = findNearbyDistributors(form);
		if (CollectionUtils.isNotEmpty(nearByDistributors)) {
			String customerId = updateCustomerInfo(form);
			orderId = placeOrder(form, customerId);
			for (Distributor distributor : nearByDistributors) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setOrderId(orderId);
				orderTransaction.setCustomerId(customerId);
				orderTransaction
						.setDistributorId(distributor.getEmailAddress());

				transactionDao.addOrderTransaction(orderTransaction);
			}

			// send asynchronous notification to customer
			sendNotification(form.getCustomerEmailId(),
					form.getCustomerMobileNumber(), messageSource.getMessage(
							"order.confirmation.notification.subject",
							new Object[0], Locale.ENGLISH),
					messageSource.getMessage(
							"order.confirmation.notification.message",
							new Object[] { orderId }, Locale.ENGLISH));
			LOGGER.debug("The order with ref no " + orderId
					+ " is placed successfully.");
		}

		return orderId;
	}

	/**
	 * Place the order for given customer
	 * 
	 * @param form
	 *            An instance of {@link OrderMedicineForm}
	 * @param customerId
	 *            The id of the customer who placed the order
	 * @return The id of the placed order
	 */
	private String placeOrder(OrderMedicineForm form, String customerId) {
		String orderId = RandomStringUtils.randomAlphanumeric(12);
		Order order = new Order();
		order.setId(orderId);
		order.setCustomerId(customerId);
		order.setPatient(form.getPatientName());
		order.setDoctor(form.getDoctorName());
		String[] medicineNames = form.getMedicineName().split(",");
		String[] medicinePowers = form.getMedicinePower().split(",");
		String[] medicineQuantities = form.getMedicineQuantity().split(",");
		for (int index = 0; index < medicineNames.length; index++) {
			Medicine medicine = new Medicine();
			medicine.setName(medicineNames[index]);
			medicine.setPower(medicinePowers[index]);
			medicine.setQuantity(Integer.parseInt(medicineQuantities[index]));
			order.addMedicine(medicine);
		}
		orderDao.addOrder(order);
		return orderId;
	}

	/**
	 * Update the customer information in database
	 * 
	 * @param form
	 *            An instance of {@link OrderMedicineForm}
	 * @return The id of the customer
	 */
	private String updateCustomerInfo(OrderMedicineForm form) {
		Customer customer = new Customer();
		customer.setName(form.getCustomerName());
		customer.setEmailId(form.getCustomerEmailId());
		customer.setMobileNumber(form.getCustomerMobileNumber());
		Address address = new Address();
		address.setHouseOrFlatNumber(form.getCustomerHouseNumber());
		address.setStreetAddress(form.getCustomerHouseStreetAddress());
		address.setLocality(form.getCustomerHouseLocality());
		address.setCity(form.getCustomerHouseCity());
		address.setPin(form.getCustomerHousePin());
		customer.setAddress(address);

		customerDao.addOrUpdateCustomer(customer);
		return customer.getEmailId();
	}

	/**
	 * Find nearby distributor in same locality or same pin
	 * 
	 * @param form
	 *            An instance of {@link OrderMedicineForm}
	 * @return A list of distributor
	 */
	private List<Distributor> findNearbyDistributors(OrderMedicineForm form) {
		// first try to search the stores in same locality
		List<Distributor> nearByDistributors = distributorDao
				.getDistributorsByLocality(form.getCustomerHouseLocality(),
						form.getCustomerHouseCity());
		// if not found try to search the stores in same pin
		if (CollectionUtils.isEmpty(nearByDistributors)) {
			nearByDistributors = distributorDao.getDistributorsByPin(
					form.getCustomerHousePin(), form.getCustomerHouseCity());
		}
		return nearByDistributors;
	}

}
