package com.notifcation.service.main;

import java.util.List;

import org.json.simple.JSONObject;

import com.notification.service.model.FCMNotification;

public interface FCMNotificationService {

	public List<FCMNotification> getFCMNotificationList();
	
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage );
	
	public int updateStatusSendNotification( long notificationId );
	
	public JSONObject sendToFCM( String fcmToken,
								 String title,
								 String message,
								 String icon,
								 String data);
	
	
}
