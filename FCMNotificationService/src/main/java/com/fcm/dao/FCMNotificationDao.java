package com.fcm.dao;

import java.util.List;

import com.fcm.model.FCMNotification;

public interface FCMNotificationDao {

	public List<FCMNotification> getFCMNotificationList();
	public int updateStatusSendFCMNotification( long notificationId );
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage );
	
}
