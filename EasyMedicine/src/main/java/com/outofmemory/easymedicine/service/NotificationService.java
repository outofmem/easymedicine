/**
 * 
 */
package com.outofmemory.easymedicine.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.outofmemory.easymedicine.notification.Notification;
import com.outofmemory.easymedicine.notification.NotificationListener;

/**
 * The business logic for sending all the notification are written here. The
 * service which requires to send notification should extend this class and use
 * its behavior to send the notification.
 * 
 * @author pribiswas
 * 
 */
public abstract class NotificationService {

	@Autowired
	protected Collection<NotificationListener> notificationListeners;

	/**
	 * Send the notification to listeners
	 * 
	 * @param email
	 *            The e-mail of the recipient
	 * @param mobile
	 *            The mobile of the recipient
	 * @param subject
	 *            The e-mail subject
	 * @param message
	 *            The detail message
	 */
	protected void sendNotification(String email, String mobile,
			String subject, String message) {
		Notification notification = new Notification(email, mobile, subject,
				message);
		sendNotification(notification);
	}

	/**
	 * Send the given notification to listeners
	 * 
	 * @param notification
	 *            An instance of {@link Notification}
	 */
	protected void sendNotification(Notification notification) {
		for (NotificationListener listener : notificationListeners) {
			listener.notify(notification);
		}
	}
}
