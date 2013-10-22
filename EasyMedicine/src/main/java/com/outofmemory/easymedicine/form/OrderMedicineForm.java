/**
 * 
 */
package com.outofmemory.easymedicine.form;

/**
 * The medicine order form
 * 
 * @author pribiswas
 * 
 */
public class OrderMedicineForm {

	private String customerName;
	private String customerEmailId;
	private String customerMobileNumber;
	private String customerHouseNumber;
	private String customerHouseStreetAddress;
	private String customerHouseLocality;
	private String customerHouseCity;
	private String customerHousePin;
	private String medicineName; // This is comma separated name of multiple
									// medicines
	private String medicinePower; // This is comma separated power of multiple
									// medicines
	private String medicineQuantity; // This is comma separated quantity of
										// multiple medicines
	private String patientName;
	private String doctorName;

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the customerEmailId
	 */
	public String getCustomerEmailId() {
		return customerEmailId;
	}

	/**
	 * @param customerEmailId
	 *            the customerEmailId to set
	 */
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	/**
	 * @return the customerMobileNumber
	 */
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}

	/**
	 * @param customerMobileNumber
	 *            the customerMobileNumber to set
	 */
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	/**
	 * @return the customerHouseNumber
	 */
	public String getCustomerHouseNumber() {
		return customerHouseNumber;
	}

	/**
	 * @param customerHouseNumber
	 *            the customerHouseNumber to set
	 */
	public void setCustomerHouseNumber(String customerHouseNumber) {
		this.customerHouseNumber = customerHouseNumber;
	}

	/**
	 * @return the customerHouseStreetAddress
	 */
	public String getCustomerHouseStreetAddress() {
		return customerHouseStreetAddress;
	}

	/**
	 * @param customerHouseStreetAddress
	 *            the customerHouseStreetAddress to set
	 */
	public void setCustomerHouseStreetAddress(String customerHouseStreetAddress) {
		this.customerHouseStreetAddress = customerHouseStreetAddress;
	}

	/**
	 * @return the customerHouseLocality
	 */
	public String getCustomerHouseLocality() {
		return customerHouseLocality;
	}

	/**
	 * @param customerHouseLocality
	 *            the customerHouseLocality to set
	 */
	public void setCustomerHouseLocality(String customerHouseLocality) {
		this.customerHouseLocality = customerHouseLocality;
	}

	/**
	 * @return the customerHouseCity
	 */
	public String getCustomerHouseCity() {
		return customerHouseCity;
	}

	/**
	 * @param customerHouseCity
	 *            the customerHouseCity to set
	 */
	public void setCustomerHouseCity(String customerHouseCity) {
		this.customerHouseCity = customerHouseCity;
	}

	/**
	 * @return the customerHousePin
	 */
	public String getCustomerHousePin() {
		return customerHousePin;
	}

	/**
	 * @param customerHousePin
	 *            the customerHousePin to set
	 */
	public void setCustomerHousePin(String customerHousePin) {
		this.customerHousePin = customerHousePin;
	}

	/**
	 * @return the medicineName
	 */
	public String getMedicineName() {
		return medicineName;
	}

	/**
	 * @param medicineName
	 *            the medicineName to set
	 */
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}

	/**
	 * @return the medicinePower
	 */
	public String getMedicinePower() {
		return medicinePower;
	}

	/**
	 * @param medicinePower
	 *            the medicinePower to set
	 */
	public void setMedicinePower(String medicinePower) {
		this.medicinePower = medicinePower;
	}

	/**
	 * @return the medicineQuantity
	 */
	public String getMedicineQuantity() {
		return medicineQuantity;
	}

	/**
	 * @param medicineQuantity
	 *            the medicineQuantity to set
	 */
	public void setMedicineQuantity(String medicineQuantity) {
		this.medicineQuantity = medicineQuantity;
	}

	/**
	 * @return the patientName
	 */
	public String getPatientName() {
		return patientName;
	}

	/**
	 * @param patientName
	 *            the patientName to set
	 */
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	/**
	 * @return the doctorName
	 */
	public String getDoctorName() {
		return doctorName;
	}

	/**
	 * @param doctorName
	 *            the doctorName to set
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderMedicineForm [customerName=" + customerName
				+ ", customerEmailId=" + customerEmailId
				+ ", customerMobileNumber=" + customerMobileNumber
				+ ", customerHouseNumber=" + customerHouseNumber
				+ ", customerHouseStreetAddress=" + customerHouseStreetAddress
				+ ", customerHouseLocality=" + customerHouseLocality
				+ ", customerHouseCity=" + customerHouseCity
				+ ", customerHousePin=" + customerHousePin + ", medicineName="
				+ medicineName + ", medicinePower=" + medicinePower
				+ ", medicineQuantity=" + medicineQuantity + ", patientName="
				+ patientName + ", doctorName=" + doctorName + "]";
	}

}
