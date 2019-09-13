package com.webservicemaster.iirs.service.apnnotification;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.APNNotificationRepository;
import com.webservicemaster.iirs.domain.master.APNNotification;
import com.webservicemaster.iirs.domain.master.Account;
import com.webservicemaster.iirs.domain.master.FCMNotification;
import com.webservicemaster.iirs.domain.master.Setting;
import com.webservicemaster.iirs.service.account.AccountService;
import com.webservicemaster.iirs.service.setting.SettingService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class APNNotificationServiceImpl implements APNNotificationService{

	private static final Logger LOGGER			= LoggerFactory.getLogger(APNNotificationServiceImpl.class);
	private APNNotificationRepository apnRepo;	
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Value("${db.key.md5}")
	private String dbMD5Key;
	
	@Autowired
	private Utility util;
	
	@Autowired
	private AES aes;
	
	@Value("${customer.photo.url}")
	private String customerPhotoURL;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private AccountService accService;
	
	@Value("${currency.format.language}")
	private String currencyLanguage;
	
	@Value("${currency.format.country}")
	private String currencyCountry;
	
	@Autowired
	APNNotificationServiceImpl( APNNotificationRepository repository ){
		this.apnRepo		= repository;
	}
	
	@Transactional
	@Override
	public JSONObject sendNotificationFriendRequest( String memberAPNToken,
													 long memberId,
			 										 String friendName,
			 										 String friendPhoto,
			 										 short needAccept,
			 										 long notificationId) throws ParseException{
		
		
		JSONObject joResult			= new JSONObject();
		String title 				= "";
		String body 				= "";
		String icon 				= "";
		String data 				= "";
		
		JSONObject joData			= new JSONObject();
		
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
		body 						= body.replace("[#friend_name#]", friendName);
		icon 						= customerPhotoURL + friendPhoto;
		
		List lTotalFR 				= apnRepo.getTotalPendingRequest(memberId);
		List lTotalFL 				= apnRepo.getTotalFriendList(memberId);
		
		joData.put("name", friendName);
		joData.put("photo", friendPhoto);
		joData.put("totalPendingRequest", Integer.parseInt( lTotalFR.get(0).toString() ));
		joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString() ));
		joData.put("fromModule", "friend");
		joData.put("msg", body);
		joData.put("needAccept", needAccept);
		
		data 						= joData.toString();
		
		APNNotification apn			= new APNNotification();
		apn.setCustomerId(memberId);
		apn.setPostDate(util.getDateTime());
		apn.setApnToken(memberAPNToken);
		apn.setTitle(title);
		apn.setMessage(body);
		apn.setIcon(icon);
		apn.setStatusSend((short)0);
		apn.setData(data);
		apn.setAyaresNotificationId(notificationId);
		manager.persist(apn);
		
		if( apn.getApnNotificationId() > 0 ){
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Notification sucessfully created.");
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Failed create notification.");
		}
		
		return joResult;
	}
	
	@Transactional
	@Override
	public JSONObject sendNotificationRebate( String memberAPNToken,
											  long memberId,
											  JSONObject joTransDetail,
											  double lastBalance,
											  long notificationId) throws ParseException{

		
		JSONObject joResult			= new JSONObject();
		String title 				= "";
		String body 				= "";
		String icon 				= "";
		String data 				= "";
		
		JSONObject joData			= new JSONObject();
		
		Account acc 				= accService.getAccountDataByMemberId(memberId);
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("rebate_notification");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}
		
		title						= title.replace("[#merchant_name#]", JSON.get(joTransDetail, "merchantName"));
		title						= title.replace("[#rebate_value#]", "Rp. " + util.numberFormat(lastBalance, currencyLanguage, currencyCountry));
		
		body 						= body.replace("[#merchant_name#]", JSON.get(joTransDetail, "merchantName"));
		body 						= body.replace("[#rebate_value#]", "Rp. " + util.numberFormat(lastBalance, currencyLanguage, currencyCountry) );
		
		joData.put("transaction", joTransDetail);
		joData.put("fromModule", "rebate");
		joData.put("msg", body);	
		joData.put("balance", lastBalance);
		
		data 						= joData.toString();
		
		/*2. Add to FCMNotification*/
		APNNotification apn			= new APNNotification();
		apn.setCustomerId(memberId);
		apn.setPostDate(util.getDateTime());
		apn.setApnToken(memberAPNToken);
		apn.setTitle(title);
		apn.setMessage(body);
		apn.setIcon("");
		apn.setStatusSend((short)0);
		apn.setData(data);
		apn.setAyaresNotificationId(notificationId);
		manager.persist(apn);
		
		if( apn.getApnNotificationId() > 0 ){
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Notification sucessfully created.");
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Failed create notification.");
		}
		
		return joResult;
	}
	
	public JSONObject sendAcceptNotificationFriendRequest( String memberAPNToken,
														   long memberId,
														   String friendName,
														   String friendPhoto) throws ParseException{
		
		JSONObject joResult			= new JSONObject();
		String title 				= "";
		String body 				= "";
		String icon 				= "";
		String data 				= "";
		
		JSONObject joData			= new JSONObject();
		
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
		icon 						= customerPhotoURL + friendPhoto;
		
		List lTotalFR 				= apnRepo.getTotalPendingRequest(memberId);
		List lTotalFL 				= apnRepo.getTotalFriendList(memberId);
		
		joData.put("name", friendName);
		joData.put("photo", friendPhoto);
		joData.put("totalPendingRequest", Integer.parseInt( lTotalFR.get(0).toString() ));
		joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString() ));
		joData.put("fromModule", "friend");
		joData.put("msg", body);
		
		data 						= joData.toString();
		
		APNNotification apn 		= new APNNotification();
		apn.setCustomerId(memberId);
		apn.setPostDate(util.getDateTime());
		apn.setApnToken(memberAPNToken);
		apn.setTitle(title);
		apn.setMessage(body);
		apn.setIcon(icon);
		apn.setStatusSend((short)0);
		apn.setData(data);
		manager.persist(apn);
		
		if( apn.getApnNotificationId() > 0 ){
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Notification sucessfully created.");
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Failed create notification.");
		}
		
		return joResult;
		
	}
	
}
