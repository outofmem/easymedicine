/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The model class that holds the information about medicine
 * 
 * @author pribiswas
 * 
 */
public class Medicine {

	private String name;
	private String power;
	private int quantity;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the power
	 */
	public String getPower() {
		return power;
	}

	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(String power) {
		this.power = power;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return new StringBuilder("name:").append(name).append(",power:")
				.append(power).append(",quantity:").append(quantity).toString();
	}

}