package com.webservicemaster.iirs.service.fcmnotification;

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

import com.webservicemaster.iirs.dao.master.CustomerRepository;
import com.webservicemaster.iirs.dao.master.FCMNotificationRepository;
import com.webservicemaster.iirs.domain.master.Account;
import com.webservicemaster.iirs.domain.master.FCMNotification;
import com.webservicemaster.iirs.domain.master.Setting;
import com.webservicemaster.iirs.service.account.AccountService;
import com.webservicemaster.iirs.service.customer.CustomerServiceImpl;
import com.webservicemaster.iirs.service.setting.SettingService;
import com.webservicemaster.iirs.service.smsemailqueue.SMSEmailService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class FCMNotificationServiceImpl implements FCMNotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	private FCMNotificationRepository fcmRepo;
	
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
	FCMNotificationServiceImpl(FCMNotificationRepository repository) {
        this.fcmRepo = repository;
    }
	
	@Transactional
	@Override
	public JSONObject sendNotificationFriendRequest( String memberFCMToken,
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
		
		List lTotalFR 				= fcmRepo.getTotalPendingRequest(memberId);
		List lTotalFL 				= fcmRepo.getTotalFriendList(memberId);
		
		joData.put("name", friendName);
		joData.put("photo", friendPhoto);
		joData.put("totalPendingRequest", Integer.parseInt( lTotalFR.get(0).toString() ));
		joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString() ));
		joData.put("fromModule", "friend");
		joData.put("msg", body);
		joData.put("needAccept", needAccept);
		
		data 						= joData.toString();
		
		FCMNotification fcm			= new FCMNotification();
		fcm.setCustomerId(memberId);
		fcm.setPostDate(util.getDateTime());
		fcm.setFcmToken(memberFCMToken);
		fcm.setTitle(title);
		fcm.setMessage(body);
		fcm.setIcon(icon);
		fcm.setStatusSend((short)0);
		fcm.setData(data);
		fcm.setAyaresNotificationId(notificationId);
		manager.persist(fcm);
		
		if( fcm.getNotificationId() > 0 ){
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
	public JSONObject sendAcceptNotificationFriendRequest( String memberFCMToken,
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
		
		List lTotalFR 				= fcmRepo.getTotalPendingRequest(memberId);
		List lTotalFL 				= fcmRepo.getTotalFriendList(memberId);
		
		joData.put("name", friendName);
		joData.put("photo", friendPhoto);
		joData.put("totalPendingRequest", Integer.parseInt( lTotalFR.get(0).toString() ));
		joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString() ));
		joData.put("fromModule", "friend");
		joData.put("msg", body);
		
		data 						= joData.toString();
		
		FCMNotification fcm			= new FCMNotification();
		fcm.setCustomerId(memberId);
		fcm.setPostDate(util.getDateTime());
		fcm.setFcmToken(memberFCMToken);
		fcm.setTitle(title);
		fcm.setMessage(body);
		fcm.setIcon(icon);
		fcm.setStatusSend((short)0);
		fcm.setData(data);
		manager.persist(fcm);
		
		if( fcm.getNotificationId() > 0 ){
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
	public JSONObject sendNotificationRebate( String memberFCMToken,
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
		FCMNotification fcm			= new FCMNotification();
		fcm.setCustomerId(memberId);
		fcm.setPostDate(util.getDateTime());
		fcm.setFcmToken(memberFCMToken);
		fcm.setTitle(title);
		fcm.setMessage(body);
		fcm.setIcon("");
		fcm.setStatusSend((short)0);
		fcm.setData(data);
		fcm.setAyaresNotificationId(notificationId);
		manager.persist(fcm);
		
		if( fcm.getNotificationId() > 0 ){
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
	public JSONObject sendNotificationRedeem( String memberFCMToken,
											  long memberId,
											  String merchantName,
											  double lastBalance,
											  long voucherId) throws ParseException{

		
		JSONObject joResult			= new JSONObject();
		String title 				= "";
		String body 				= "";
		String icon 				= "";
		String data 				= "";
		
		JSONObject joData			= new JSONObject();
		
		Account acc 				= accService.getAccountDataByMemberId(memberId);
		
		/*1. Get notification content*/
		List<Setting> lSetting		= settingService.findBySettingPurpose("redeem_notification");
		for( Setting dSetting : lSetting ){			
			if( dSetting.getSettingType().equals("title") ){				
				title 				= dSetting.getValue();
			}else if( dSetting.getSettingType().equals("body") ){
				body 				= dSetting.getValue();
			}			
		}
		
		title						= title.replace("[#merchant_name#]", merchantName);
		body 						= body.replace("[#merchant_name#]", merchantName);
		body 						= body.replace("[#rebate_value#]", "Rp. " + util.numberFormat(lastBalance, currencyLanguage, currencyCountry));
		
		joData.put("fromModule", "voucher");
		joData.put("msg", body);	
		joData.put("balance", lastBalance);
		joData.put("voucherId", voucherId);
		
		data 						= joData.toString();
		
		FCMNotification fcm			= new FCMNotification();
		fcm.setCustomerId(memberId);
		fcm.setPostDate(util.getDateTime());
		fcm.setFcmToken(memberFCMToken);
		fcm.setTitle(title);
		fcm.setMessage(body);
		fcm.setIcon("");
		fcm.setStatusSend((short)0);
		fcm.setData(data);
		manager.persist(fcm);
		
		if( fcm.getNotificationId() > 0 ){
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Notification sucessfully created.");
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Failed create notification.");
		}
		
		return joResult;
	}
	
}
