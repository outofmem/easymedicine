/**
 * 
 */
package com.outofmemory.easymedicine.service;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.outofmemory.easymedicine.dao.CustomerDao;
import com.outofmemory.easymedicine.dao.DistributorDao;
import com.outofmemory.easymedicine.dao.OrderDao;
import com.outofmemory.easymedicine.dao.TransactionDao;
import com.outofmemory.easymedicine.model.Customer;
import com.outofmemory.easymedicine.model.Distributor;
import com.outofmemory.easymedicine.model.Medicine;
import com.outofmemory.easymedicine.model.Order;
import com.outofmemory.easymedicine.model.OrderQueueItem;
import com.outofmemory.easymedicine.model.OrderTransaction;

/**
 * This class maintains an order queue where each order is placed. It polls the
 * queue periodically and process the first item if its time frame expires the
 * specified time amount and after processing it removes that item from queue.
 * 
 * @author pribiswas
 * 
 */
@Service
public class OrderQueueProcessor extends NotificationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderQueueProcessor.class);
	private static final long ORDER_PROCESSING_DELAY_IN_MILLIS = 15 * 60 * 1000;
	// According to JDK documentation, ArrayDeque performs faster than
	// LinkedList if it is used as Queue
	private final Queue<OrderQueueItem> orderQueue = new ArrayDeque<OrderQueueItem>();
	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private DistributorDao distributorDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MessageSource messageSource;

	public void addToOrderQueue(OrderQueueItem orderQueueItem) {
		orderQueue.add(orderQueueItem);
	}

	/**
	 * You should not call this method. Spring container will execute it
	 * periodically(in each minute).
	 */
	@Scheduled(fixedDelay = 60000)
	public void processQueue() {
		OrderQueueItem firstOrderItem = orderQueue.peek();
		if (firstOrderItem != null) {
			Date currentTime = new Date();
			Date orderTime = firstOrderItem.getOrderTime();
			if (currentTime.getTime() - orderTime.getTime() > ORDER_PROCESSING_DELAY_IN_MILLIS) {
				serveOrder(firstOrderItem);
				orderQueue.remove();
			}
		}
	}

	/**
	 * The main business logic that selects the winner amongst the bidders of
	 * the provided order.
	 * 
	 * @param orderItem
	 *            An instance of {@link OrderQueueItem}
	 */
	private void serveOrder(OrderQueueItem orderItem) {
		List<OrderTransaction> transactions = transactionDao
				.getTransactionsByOrderIdSortedByCost(orderItem.getOrderId(),
						true);
		// The transaction having lowest non zero price win the bid
		OrderTransaction selectedTransaction = null;
		int noOfTxs = transactions.size();
		for (int txIndex = 0; txIndex < noOfTxs; txIndex++) {
			if (transactions.get(txIndex).getCost() != null
					&& transactions.get(txIndex).getCost() > 0) {
				selectedTransaction = transactions.remove(txIndex);
				break;
			}
		}
		// remove rest of the transactions from the collection
		transactionDao.removeTransactions(transactions
				.toArray(new OrderTransaction[0]));
		// notify the stake holders(selected store and customer) about the
		// processed order
		if (selectedTransaction == null) {
			// no bidders found, so just populate the orderId and customerId for
			// notification purpose
			selectedTransaction = new OrderTransaction();
			selectedTransaction.setCustomerId(transactions.get(0)
					.getCustomerId());
			selectedTransaction.setOrderId(transactions.get(0).getOrderId());
		}
		notifyStakeHolders(selectedTransaction);
		LOGGER.debug(orderItem + " processed successfully...");
	}

	/**
	 * Notify the stake holders (selected medicine store and customer) regarding
	 * the delivery of the order.
	 * 
	 * @param selectedTransaction
	 *            An instance of {@link OrderTransaction}
	 */
	private void notifyStakeHolders(OrderTransaction selectedTransaction) {
		Customer customer = customerDao
				.findCustomerByEmailId(selectedTransaction.getCustomerId());
		if (selectedTransaction.getOrderId() != null) {
			// A transaction is selected from database
			String orderDetails = getOrderDetails(selectedTransaction);
			String customerDetails = new StringBuilder("Name : ")
					.append(customer.getName()).append("\nMobile : ")
					.append(customer.getMobileNumber()).append("\nAddress : ")
					.append(customer.getAddress().toString()).toString();

			Distributor distributor = distributorDao
					.findDistributorByEmailId(selectedTransaction
							.getDistributorId());
			String distributorDetails = new StringBuilder("Store : ")
					.append(distributor.getMedicineStore().toString())
					.append("\nMobile : ")
					.append(distributor.getMobileNumber()).toString();

			// send order serving notification to distributor
			sendNotification(distributor.getEmailAddress(),
					distributor.getMobileNumber(), messageSource.getMessage(
							"order.serving.notification.subject",
							new Object[] { selectedTransaction.getOrderId() },
							Locale.ENGLISH), messageSource.getMessage(
							"order.serving.notification.message", new Object[] {
									orderDetails, customerDetails },
							Locale.ENGLISH));
			// send order delivery notification to customer
			sendNotification(customer.getEmailId(), customer.getMobileNumber(),
					messageSource.getMessage(
							"order.delivery.notification.subject",
							new Object[] { selectedTransaction.getOrderId() },
							Locale.ENGLISH), messageSource.getMessage(
							"order.delivery.notification.message",
							new Object[] { orderDetails, distributorDetails },
							Locale.ENGLISH));
		} else {
			// No distributor places bid for that order. Notify the customer
			// about that.
			sendNotification(customer.getEmailId(), customer.getMobileNumber(),
					messageSource.getMessage(
							"order.delivery.failure.notification.subject",
							new Object[] { selectedTransaction.getOrderId() },
							Locale.ENGLISH), messageSource.getMessage(
							"order.delivery.failure.notification.message",
							new Object[] { selectedTransaction.getOrderId() },
							Locale.ENGLISH));
		}
	}

	/**
	 * Get the order details from the selected transaction
	 * 
	 * @param selectedTransaction
	 * @return The order details
	 */
	private String getOrderDetails(OrderTransaction selectedTransaction) {
		Order order = orderDao.findOrderById(selectedTransaction.getOrderId());
		StringBuilder orderBuilder = new StringBuilder("Patient : ")
				.append(order.getPatient()).append("\nDoctor : ")
				.append(order.getDoctor()).append("\nMedicines : ");
		List<Medicine> medicines = order.getMedicines();
		for (int index = 0; index < medicines.size(); index++) {
			Medicine medicine = medicines.get(index);
			orderBuilder.append(index + 1).append(". ")
					.append(medicine.toString());
		}
		orderBuilder.append("\nTotal Cost : ").append(
				selectedTransaction.getCost());
		return orderBuilder.toString();
	}
}
