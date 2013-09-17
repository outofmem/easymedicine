/**
 * 
 */
package com.outofmemory.easymedicine.notification;

/**
 * A simple POJO class to hold the notification information
 * 
 * @author pribiswas
 * 
 */
public class Notification {

	private final String emailAddress;
	private final String mobileNumber;
	private final String subject;
	private final String message;

	/**
	 * Construct an instance of {@link Notification}
	 * 
	 * @param emailAddress
	 * @param mobileNumber
	 * @param subject
	 * @param message
	 */
	public Notification(String emailAddress, String mobileNumber,
			String subject, String message) {
		super();
		this.emailAddress = emailAddress;
		this.mobileNumber = mobileNumber;
		this.subject = subject;
		this.message = message;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
