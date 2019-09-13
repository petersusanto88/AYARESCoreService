package com.apns.dao;

import java.util.List;

import com.apns.model.APNSNotification;
public interface APNSNotificationDao {

	public List<APNSNotification> getAPNSNotificationList();
	public void updateStatusSendAPNSNotification( long notificationId );
	public void updateStatusResponseAPNSNotification( long notificationId, short statusResponse, String responseMessage );
	
}
