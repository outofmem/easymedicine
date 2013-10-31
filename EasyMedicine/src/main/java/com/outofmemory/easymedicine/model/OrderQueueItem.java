/**
 * 
 */
package com.outofmemory.easymedicine.model;

import java.util.Date;

/**
 * The model class that holds Order Queue item information
 * 
 * @author pribiswas
 * 
 */
public class OrderQueueItem {
	private final String orderId;
	private final Date orderTime;

	public OrderQueueItem(String orderId, Date orderTime) {
		this.orderId = orderId;
		this.orderTime = orderTime;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @return the orderTime
	 */
	public Date getOrderTime() {
		return orderTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderQueueItem [orderId=" + orderId + ", orderTime="
				+ orderTime + "]";
	}

}
