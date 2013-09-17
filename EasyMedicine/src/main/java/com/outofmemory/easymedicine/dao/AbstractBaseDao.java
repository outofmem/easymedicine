/**
 * 
 */
package com.outofmemory.easymedicine.dao;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.outofmemory.easymedicine.util.MongoConstants;

/**
 * This is the abstract basic DAO class which should be inherited by all other
 * DAO class
 * 
 * @author pribiswas
 * 
 */
public abstract class AbstractBaseDao {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractBaseDao.class);
	protected DB easyMedicineDatabase;

	public AbstractBaseDao() {
		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			easyMedicineDatabase = mongoClient.getDB(MongoConstants.EASY_MEDICINE_DB);
		} catch (UnknownHostException e) {
			LOGGER.error("Not able to connect to mongo server!!!");
			throw new RuntimeException(e);
		}
	}
}
