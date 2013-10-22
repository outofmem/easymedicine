/**
 * 
 */
package com.outofmemory.easymedicine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class that holds an order details
 * 
 * @author pribiswas
 * 
 */
public class Order {
	private String id;
	private String customerId;
	private List<Medicine> medicines;
	private String patient;
	private String doctor;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the medicines
	 */
	public List<Medicine> getMedicines() {
		return medicines;
	}

	/**
	 * Add an {@link Medicine} into {@link Order}
	 * 
	 * @param medicine
	 *            An instance of {@link Medicine}
	 */
	public void addMedicine(Medicine medicine) {
		if (medicines == null) {
			medicines = new ArrayList<Medicine>();
		}
		medicines.add(medicine);
	}

	/**
	 * @return the patient
	 */
	public String getPatient() {
		return patient;
	}

	/**
	 * @param patient
	 *            the patient to set
	 */
	public void setPatient(String patient) {
		this.patient = patient;
	}

	/**
	 * @return the doctor
	 */
	public String getDoctor() {
		return doctor;
	}

	/**
	 * @param doctor
	 *            the doctor to set
	 */
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Order [id=" + id + ", customerId=" + customerId
				+ ", medicines=" + medicines + ", patient=" + patient
				+ ", doctor=" + doctor + "]";
	}

}
