/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The model class which contains the transaction information
 * 
 * @author pribiswas
 * 
 */
public class OrderTransaction {

	private String orderId;
	private String customerId;
	private String distributorId;
	private double cost;

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the distributorId
	 */
	public String getDistributorId() {
		return distributorId;
	}

	/**
	 * @param distributorId
	 *            the distributorId to set
	 */
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderTransaction [orderId=" + orderId + ", customerId="
				+ customerId + ", distributorId=" + distributorId + ", cost="
				+ cost + "]";
	}

}
