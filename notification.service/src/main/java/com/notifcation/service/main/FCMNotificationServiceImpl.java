package com.notifcation.service.main;

import java.util.List;

import org.json.simple.JSONObject;

import com.notification.service.Service;
import com.notification.service.model.FCMNotification;
import com.notification.service.util.JSON;

public class FCMNotificationServiceImpl implements FCMNotificationService {
	
	private static String debugLabel = "FCMNotificationThread";

	private static FCMNotificationRepository fcmRepo 	= new FCMNotificationRepository();
	
	public FCMNotificationServiceImpl(){}
	
	public List<FCMNotification> getFCMNotificationList(){
		return fcmRepo.getFCMNotificationList();
	}
	
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage ){
		return fcmRepo.updateStatusResponseFCMNotification(notificationId, 
														   statusResponse, 
														   responseMessage);		
	}
	
	public int updateStatusSendNotification( long notificationId ){
		return fcmRepo.updateStatusSendFCMNotification(notificationId);
	}
	
	public JSONObject sendToFCM( String fcmToken,
								 String title,
								 String message,
								 String icon,
								 String data){
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joBody 			= new JSONObject();
		JSONObject joNotification 	= new JSONObject();
		String fcmResult 			= "";
		
		joNotification.put("title", title);
		joNotification.put("body", message);
		joNotification.put("icon", "icon");
		
		joBody.put("to", fcmToken);
		//joBody.put("notification", joNotification);
		joBody.put("data", JSON.newJSONObject(data));
		
		fcmResult 					= Service.util.sendFCM(Service.fcmHost, 
														   joBody.toString(), 
														   "application/json",
														   Service.fcmKey);
		
		Service.writeDebug("Data : " + joBody.toString(), debugLabel);
		Service.writeDebug("Result : " + fcmResult, debugLabel);
		
		joResult 					= JSON.newJSONObject(fcmResult);
		
		return joResult;
		
	}
	
}
