/**
 * 
 */
package com.outofmemory.easymedicine.notification;

/**
 * This abstract class is used to notify user about an event. It is a classical
 * example of observer design pattern.
 * 
 * @author pribiswas
 * 
 */
public abstract class NotificationListener {

	/**
	 * Notify the event
	 * 
	 * @param notification
	 *            An instance of {@link Notification}
	 */
	public void notify(final Notification notification) {
		// sending notification is done in separate thread to boost up the
		// operational performance like registration, resetting and changing
		// password etc.
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendNotification(notification);
			}
		}).start();
	}

	/**
	 * Send the notification
	 * 
	 * @param notification
	 *            An instance of {@link Notification}
	 */
	public abstract void sendNotification(Notification notification);
}
