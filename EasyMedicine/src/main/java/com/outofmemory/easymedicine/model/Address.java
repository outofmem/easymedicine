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

	private String houseOrFlatAddress;
	private String locality;
	private String city;
	private String pin;

	/**
	 * @return the houseOrFlatAddress
	 */
	public String getHouseOrFlatAddress() {
		return houseOrFlatAddress;
	}

	/**
	 * @param houseOrFlatAddress
	 *            the houseOrFlatAddress to set
	 */
	public void setHouseOrFlatAddress(String houseOrFlatAddress) {
		this.houseOrFlatAddress = houseOrFlatAddress;
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

	@Override
	public String toString() {
		return new StringBuilder(houseOrFlatAddress).append(",")
				.append(locality).append(",").append(city).append("-")
				.append(pin).toString();
	}

}
