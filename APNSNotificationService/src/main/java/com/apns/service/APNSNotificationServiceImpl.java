package com.apns.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLException;

import org.json.simple.JSONObject;

import com.apns.dao.APNSNotificationDao;
import com.apns.dao.APNSNotificationDaoImpl;
import com.apns.main.Service;
import com.apns.model.APNSNotification;

public class APNSNotificationServiceImpl implements APNSNotificationService{

	private static APNSNotificationDao apnsDao 			= new APNSNotificationDaoImpl();
	private static String debugLabel 						= "APNSNotificationServiceImpl";
	public APNSNotificationServiceImpl() {}
	
	public List<APNSNotification> getAPNSNotificationList(){
		return apnsDao.getAPNSNotificationList();
	}
	
	public void updateStatusResponseFCMNotification( long notificationId, short statusResponse, String responseMessage ){
		apnsDao.updateStatusResponseAPNSNotification(notificationId, 
														   statusResponse, 
														   responseMessage);		
	}
	
	public void updateStatusSendNotification( long notificationId ){
		apnsDao.updateStatusSendAPNSNotification(notificationId);
	}
	
	public JSONObject sendToAPNS( String apnsToken,
									 String title,
									 String body,
									 String icon,
									 JSONObject data ) throws SSLException, IOException {
		
		JSONObject joResult 		= new JSONObject();
		
		joResult 					= Service.util.sendAPNS(apnsToken, title, body, data);
		
		Service.writeDebug("==============================================================", debugLabel);
		Service.writeDebug("Request to APNS --->", debugLabel);
		Service.writeDebug("APNS Token : " + apnsToken, debugLabel);
		Service.writeDebug("Title : " + title, debugLabel);
		Service.writeDebug("Body : " + body, debugLabel);
		Service.writeDebug("Icon : " + icon, debugLabel);
		Service.writeDebug("JSON Data : " + data, debugLabel);
		Service.writeDebug("<--- Response From APNS", debugLabel);
		Service.writeDebug("Result : " + joResult.toString(), debugLabel);
		
		return joResult;
		
	}
	
}
