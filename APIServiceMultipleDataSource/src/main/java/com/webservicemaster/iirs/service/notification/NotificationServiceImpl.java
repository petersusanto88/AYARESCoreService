package com.webservicemaster.iirs.service.notification;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.NotificationRepository;
import com.webservicemaster.iirs.domain.master.Notification;
import com.webservicemaster.iirs.domain.master.Setting;
import com.webservicemaster.iirs.service.setting.SettingService;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	private  static final Logger LOGGER			= LoggerFactory.getLogger(NotificationServiceImpl.class);
	private NotificationRepository notifRepo;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private Utility util;
	
	@Value("${currency.format.language}")
	private String currencyLanguage;
	
	@Value("${currency.format.country}")
	private String currencyCountry;
	
	
	@Autowired
	public NotificationServiceImpl( NotificationRepository repo ) {
		this.notifRepo = repo;
	}
	
	@Override
	public JSONObject addToNotificationGetRebate( long customerId,
										 		  String notificationType,
										 		  String icon,
										 		  String merchantName,
										 		  String merchantAddress,
										 		  float latitude,
										 		  float longitude,
										 		  Double lastBalance) throws ParseException {
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joData 			= new JSONObject();
		
		/*Construct message*/
		JSONObject joMessage 		= this.constructMessageNotificationGetRebate(merchantName, 
																				 lastBalance,
																				 icon);	
		
		joData.put("merchantName", merchantName);
		joData.put("merchantAddress", merchantAddress);
		joData.put("merchantLatitude", latitude);
		joData.put("merchantLongitude", longitude);
		
		/*Add to Notification*/
		joResult 					= this.addToNotification(customerId, 
															 notificationType, 
															 JSON.get(joMessage, "icon"), 
															 JSON.get(joMessage, "title"), 
															 JSON.get(joMessage, "body"),
															 joData);		
		
		return joResult;
		
	}
	
	@Override
	public JSONObject addToNotificationSendFriendRequest( long customerId,
														  String icon,
														  String friendName) throws ParseException {
		
		JSONObject joResult 		= new JSONObject();
		
		/*Construct message*/
		JSONObject joMessage 		= this.constructMessageNotificationFriendRequest(icon, 
																				 	 friendName);	
		
		/*Add to Notification*/
		joResult 					= this.addToNotification(customerId, 
															 "friend_request", 
															 icon, 
															 JSON.get(joMessage, "title"), 
															 JSON.get(joMessage, "body"),
															 (new JSONObject()));		
		
		return joResult;
		
	}
	
	@Override
	public JSONObject addToNotificationAcceptFriendRequest( long customerId, String icon, String friendName ) throws ParseException {
		
		JSONObject joResult 		= new JSONObject();
		
		/*Construct message*/
		JSONObject joMessage 		= this.constructMessageNotificationAcceptFriendRequest(customerId, icon, friendName);	
		
		/*Add to Notification*/
		joResult 					= this.addToNotification(customerId, 
															 "accept_friend_request", 
															 JSON.get(joMessage, "icon"), 
															 JSON.get(joMessage, "title"), 
															 JSON.get(joMessage, "body"),
															 (new JSONObject()));		
		
		return joResult;
		
	}
	
	@Transactional
	private JSONObject addToNotification( long customerId,
										 String notificationType,
										 String icon,
										 String subject,
										 String body,
										 JSONObject joData) throws ParseException {
		
		JSONObject joResult 		= new JSONObject();
		
		Notification notif 			= new Notification();
		notif.setCustomerId(customerId);
		notif.setNotificationDate(util.getDateTime());
		notif.setNotificationType(notificationType);
		notif.setIcon(icon);
		notif.setSubject(subject);
		notif.setBody(body);
		notif.setHasRead((short)0);
		notif.setData(joData.toString());
		manager.persist(notif);
		
		if( notif.getNotificationId() > 0 ) {
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Successfully add notification");
			joResult.put("notificationId", notif.getNotificationId());
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Add Notifcation failed");
			joResult.put("notificationId", 0);
		}
		
		return joResult;
		
	}
	
	private JSONObject constructMessageNotificationFriendRequest( String icon, String friendName ) {
		
		JSONObject joResult 		= new JSONObject();
		String title = "", body		= "";
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("friend_request_notification");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}
		
		title						= title.replace("[#friend_name#]", friendName);		
		body 						= body.replace("[#friend_name#]",friendName);
		
		joResult.put("title", title);
		joResult.put("body", body);
		joResult.put("icon", icon);
		
		
		return joResult;
		
	}
	
	private JSONObject constructMessageNotificationGetRebate( String merchantName,
															  Double lastBalance,
															  String merchantIcon) {
		
		JSONObject joResult 		= new JSONObject();
		String title = "", body		= "";
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("rebate_notification");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}
		
		title						= title.replace("[#merchant_name#]", merchantName);
		title						= title.replace("[#rebate_value#]", "Rp. " + util.numberFormat(lastBalance, currencyLanguage, currencyCountry));
		
		body 						= body.replace("[#merchant_name#]",merchantName);
		body 						= body.replace("[#rebate_value#]", "Rp. " + util.numberFormat(lastBalance, currencyLanguage, currencyCountry) );
		
		
		joResult.put("title", title);
		joResult.put("body", body);
		joResult.put("icon", merchantIcon);
		
		return joResult;
		
	}
	
	private JSONObject constructMessageNotificationAcceptFriendRequest( long customerId,
															  			 String icon,
															  			 String friendName) {

		JSONObject joResult 		= new JSONObject();
		
		String title 				= "";
		String body 				= "";
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("friend_accept_notification");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}

		title						= title.replace("[#friend_name#]", friendName);
		body 						= body.replace("[#friend_name#]", friendName);
		
		joResult.put("title", title);
		joResult.put("body", body);
		joResult.put("icon", icon);
		
		return joResult;

	}
	
	@Override
	public JSONObject getNotificationList( long userId, String keyword, int page, int limit ) {
		
		JSONObject joResult 					= new JSONObject();		
		JSONObject joData 						= new JSONObject();
		JSONArray jaData 						= new JSONArray();
		
		if( page != 0 && page > 0 ) {
			
			page 								= Math.round( page / limit );
			
		}
		
		List<Object[]> lNotification			= notifRepo.findNotification(keyword, userId, new PageRequest( page, limit ));
		List<Notification> lNotReadNotification = notifRepo.findByHasRead( (short)0 );
		
		if( lNotification.size() > 0 ) {
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			
			Iterator<Object[]> it 				= lNotification.iterator();
			
			while( it.hasNext() ) {
				
				Object[] result 				= (Object[])it.next();
				joData 							= new JSONObject();
				
				joData.put("notificationId", (Long)result[0]);
				joData.put("customerId", (Long)result[1]);
				joData.put("notificationDate", ((Date)result[2]).toString());
				joData.put("notificationType", ((String)result[3]).toString());
				joData.put("icon", (String)result[8]);
				joData.put("subject", (String)result[4]);
				joData.put("body", (String)result[5]);
				joData.put("hasRead", (Short)result[6] );
				joData.put("data", JSON.newJSONObject( (String)result[9] ));
				
				jaData.add(joData);
				
			}
			
			joResult.put("data", jaData);
			
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
		}
		
		return joResult;
		
	}	
	
	@Override
	@Transactional
	public JSONObject deleteNotification( long userId ) {
		
		JSONObject joResult 		= new JSONObject();		
		
		notifRepo.deleteNotification(userId);
		
		joResult.put("errCode", "00");
		joResult.put("errMsg","Successfully delete notification");
		
		
		
		return joResult;	
		
	}
	
	@Override
	public int notificationNotYetRead() {
		
		int totalNotificationNotRead 			= 0;
		
		List<Notification> lNotification 		= notifRepo.findByHasRead( (short)0 );
		totalNotificationNotRead 				= lNotification.size();
		
		return totalNotificationNotRead;
		
	}
	
	private JSONObject constructMessageNotificationGetFreeRebate( long customerId, String customerName ) {
		
		JSONObject joResult = new JSONObject();
		String title = "", body = "";
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("get_free_rebate");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}
		
		title						= title.replace("[#customer_name#]", customerName);		
		body 						= body.replace("[#customer_name#]",customerName);
		
		joResult.put("title", title);
		joResult.put("body", body);	
		
		return joResult;
	}
	
}
