/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The model class to hold the customer metadata
 * 
 * @author pribiswas
 * 
 */
public class Customer {

	private String name;
	private String emailId;
	private String mobileNumber;
	private Address address;

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
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return new StringBuilder("name:").append(name).append(",emailId:")
				.append(emailId).append(",mobile:").append(mobileNumber)
				.append(",address:").append(address.toString()).toString();
	}

}
