/**
 * 
 */
package com.outofmemory.easymedicine.util;

/**
 * This class holds all the constants related to the mongo database
 * 
 * @author pribiswas
 * 
 */
public final class MongoConstants {

	private MongoConstants() {
		// Nobody can instantiate this class as it is meant to be used in static
		// way
	}

	public static final String EASY_MEDICINE_DB = "easyMedicine";

	public static final String DISTRIBUTOR_COLLECTION = "distributors";

	// The keys for distributors collection
	public static final String KEY_ID = "_id";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_MOBILE = "mobile";
	public static final String KEY_STORE = "store";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_LOCALITY = "locality";
	public static final String KEY_CITY = "city";
	public static final String KEY_PIN = "pin";

	// Mongo DB operators
	public static final String OPERATOR_SET = "$set";
}
