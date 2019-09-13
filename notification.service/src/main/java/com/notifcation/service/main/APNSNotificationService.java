package com.notifcation.service.main;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import org.json.simple.JSONObject;

import com.notification.service.model.APNSNotification;

public interface APNSNotificationService {

	public List<APNSNotification> getAPNSNotificationList();
	public int updateStatusResponseFCMNotification( long notificationId, int statusResponse, String responseMessage );
	public int updateStatusSendNotification( long notificationId );
	public JSONObject sendToAPNS( String apnsToken,
								  String title,
								  String message,
								  String icon,
								  JSONObject data ) throws UnrecoverableKeyException, KeyManagementException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException ;
	
}
