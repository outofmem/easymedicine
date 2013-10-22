/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The model class that holds the address information
 * 
 * @author pribiswas
 * 
 */
public class Address {

	private String houseOrFlatNumber;
	private String streetAddress;
	private String locality;
	private String city;
	private String pin;

	/**
	 * @return the houseOrFlatNumber
	 */
	public String getHouseOrFlatNumber() {
		return houseOrFlatNumber;
	}

	/**
	 * @param houseOrFlatNumber
	 *            the houseOrFlatNumber to set
	 */
	public void setHouseOrFlatNumber(String houseOrFlatNumber) {
		this.houseOrFlatNumber = houseOrFlatNumber;
	}

	/**
	 * @return the streetAddress
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @param streetAddress
	 *            the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality
	 *            the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin
	 *            the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Address [houseOrFlatNumber=" + houseOrFlatNumber
				+ ", streetAddress=" + streetAddress + ", locality=" + locality
				+ ", city=" + city + ", pin=" + pin + "]";
	}

}
