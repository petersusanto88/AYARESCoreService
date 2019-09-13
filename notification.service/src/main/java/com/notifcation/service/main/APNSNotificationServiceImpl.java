package com.notifcation.service.main;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLException;

import org.json.simple.JSONObject;

import com.notification.service.Service;
import com.notification.service.model.APNSNotification;
import com.notification.service.model.FCMNotification;

public class APNSNotificationServiceImpl implements APNSNotificationService{

	private static APNSNotificationRepository apnsRepo 		= new APNSNotificationRepository();
	private static String debugLabel 						= "APNSNotificationServiceImpl";
	public APNSNotificationServiceImpl() {}
	
	public List<APNSNotification> getAPNSNotificationList(){
		return apnsRepo.getAPNSNotificationList();
	}
	
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage ){
		return apnsRepo.updateStatusResponseAPNSNotification(notificationId, 
														   statusResponse, 
														   responseMessage);		
	}
	
	public int updateStatusSendNotification( long notificationId ){
		return apnsRepo.updateStatusSendAPNSNotification(notificationId);
	}
	
	public JSONObject sendToAPNS( String apnsToken,
									 String title,
									 String body,
									 String icon,
									 JSONObject data ) throws SSLException, IOException {
		
		JSONObject joResult 		= new JSONObject();
		
		joResult 					= Service.util.sendAPNS(apnsToken, title, body, data);
		
		Service.writeDebug("### JO RESULT : " + joResult, debugLabel);
		
		return joResult;
		
	}
	
}
