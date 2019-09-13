package com.apns.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import org.json.simple.JSONObject;

import com.apns.model.APNSNotification;

public interface APNSNotificationService {

	public List<APNSNotification> getAPNSNotificationList();
	public void updateStatusResponseFCMNotification( long notificationId, short statusResponse, String responseMessage );
	public void updateStatusSendNotification( long notificationId );
	public JSONObject sendToAPNS( String apnsToken,
								  String title,
								  String message,
								  String icon,
								  JSONObject data ) throws UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException ;
	
}
