package com.webservicemaster.iirs.service.notification;

import java.text.ParseException;
import java.util.Date;

import org.json.simple.JSONObject;

public interface NotificationService {
	
	public JSONObject addToNotificationGetRebate( long customerId,
										 		  String notificationType,
										 		  String icon,
										 		  String merchantName,
										 		  String merchantAddress,
										 		  float latitude,
										 		  float longitude,
										 		  Double lastBalance) throws ParseException;
	
	public JSONObject addToNotificationSendFriendRequest( long customerId,
														  String icon,
														  String friendName) throws ParseException ;
	
	public JSONObject addToNotificationAcceptFriendRequest( long customerId, String icon, String friendName ) throws ParseException;
	
	public JSONObject getNotificationList( long userId, String keyword, int page, int limit );
	
	public JSONObject deleteNotification( long userId );
	
	public int notificationNotYetRead();
	
}
