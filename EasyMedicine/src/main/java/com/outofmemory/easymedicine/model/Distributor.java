/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The model class which contains the metadata of an distributor
 * 
 * @author pribiswas
 * 
 */
public class Distributor {

	private String emailAddress;
	private String password;
	private String mobileNumber;
	private MedicineStore medicineStore;

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the medicineStore
	 */
	public MedicineStore getMedicineStore() {
		return medicineStore;
	}

	/**
	 * @param medicineStore
	 *            the medicineStore to set
	 */
	public void setMedicineStore(MedicineStore medicineStore) {
		this.medicineStore = medicineStore;
	}

	@Override
	public String toString() {
		return medicineStore.toString();
	}

}