package com.webservicemaster.iirs.service.fcmnotification;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface FCMNotificationService {

	public JSONObject sendNotificationFriendRequest( String memberFCMToken,
													 long memberId,
													 String friendName,
													 String friendPhoto,
													 short needAccept,
													 long notificationId) throws ParseException;
	
	public JSONObject sendNotificationRebate( String memberFCMToken,
											  long memberId,
											  JSONObject joTransDetail,
											  double lastBalance,
											  long notificationId) throws ParseException;
	
	public JSONObject sendNotificationRedeem( String memberFCMToken,
											  long memberId,
											  String merchantName,
											  double lastBalance,
											  long voucherId) throws ParseException;
	
	public JSONObject sendAcceptNotificationFriendRequest( String memberFCMToken,
															 long memberId,
															 String friendName,
															 String friendPhoto) throws ParseException;
	
}
