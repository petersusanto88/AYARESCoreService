package com.webservicemaster.iirs.service.transaction;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.CustomerRepository;
import com.webservicemaster.iirs.dao.transaction.TransactionRepository;
import com.webservicemaster.iirs.domain.master.Account;
import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.domain.master.EVoucherCustomer;
import com.webservicemaster.iirs.domain.master.MerchantItem;
import com.webservicemaster.iirs.service.account.AccountService;
import com.webservicemaster.iirs.service.apnnotification.APNNotificationService;
import com.webservicemaster.iirs.service.customer.CustomerService;
import com.webservicemaster.iirs.service.evouchercustomer.EVoucherCustomerService;
import com.webservicemaster.iirs.service.fcmnotification.FCMNotificationService;
import com.webservicemaster.iirs.service.incentivesetting.IncentiveService;
import com.webservicemaster.iirs.service.merchant.MerchantService;
import com.webservicemaster.iirs.service.merchantitem.MerchantItemService;
import com.webservicemaster.iirs.service.merchantuseraccess.MerchantUserAccessService;
import com.webservicemaster.iirs.service.notification.NotificationService;
import com.webservicemaster.iirs.service.transactiondetail.TransactionDetailService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.EncryptorUtility;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);
	
	@PersistenceContext(unitName="transactionPU")
	private EntityManager manager;
	
	@Value("${aes.key}")
	private String aesKey;
	
	@Value("${db.key}")
	private String dbKey;
	
	@Value("${db.key.md5}")
	private String dbMD5Key;
	
	@Value("${flag.qrcode.token}")
	private short flagQRCodeToken;
	
	@Value("${merchant.photo.icon}")
	private String merchantPhotoPath;
	
	@Autowired
	private Utility util;
	
	@Autowired
	private AES aes;
	
	@Autowired
	EncryptorUtility encUtil;
	
	@Autowired
	private MerchantItemService merchantItem;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AccountService accService;
	
	@Autowired
	private TransactionRepository transRepo;
	
	@Autowired
	private EVoucherCustomerService eVoucherCustService;
	
	@Autowired
	private TransactionDetailService transDetailService;
	
	@Autowired
	private FCMNotificationService fcmService;
	
	@Autowired
	private APNNotificationService apnsService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MerchantUserAccessService merchantUserService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private IncentiveService incentiveService;
	
	@Autowired
	TransactionServiceImpl(TransactionRepository repository) {
        this.transRepo = repository;
    }
	
	@Transactional
	@Override
	public JSONObject transactionOnMerchantModel1( String qrCode,
												   int userId,
												   String merchantTransactionNumber,
												   double totalPurchase){
		
		JSONObject joResult		= new JSONObject();
		byte[] bQRCode 			= util.hexToByte( qrCode );
		String email			= "";
		long customerId 		= 0;
		String phoneNumber		= "";
		String token 			= "";
		
		String statusSP 		= "";
		String msgSP 			= "";
		String transactionNumber= "";
		
		double lastBalance  	= 0;
		
		int notificationId 		= 0;
		
		List lValidate 			= null;
		
		/*1. Validate QRCode*/
		//Format : email|customerId|phoneNumber|token
		//method => 1 : add via scan barcode on customer device 
		
		try {
			
			qrCode 					= (String)aes.decrypt(bQRCode, aesKey);
			
			System.out.println("QRCode : " + qrCode);
			
			String[] arrQRCode 		= qrCode.split("\\|");
			
			if( !arrQRCode[0].equals("") ){
				email			= arrQRCode[0];
			}
			if( !arrQRCode[1].equals("") ){
				customerId		= Long.parseLong( arrQRCode[1] );
			}
			if( !arrQRCode[2].equals("") ){
				phoneNumber		= arrQRCode[2];
			}
			
			if( flagQRCodeToken == 1 ){
				if( !arrQRCode[3].equals("") ){
					token 			= arrQRCode[3];
				}
			}
			
			/*System.out.println("Email : " + arrQRCode[0]);
			System.out.println("Customer Id : " + arrQRCode[1]);
			System.out.println("Phone Number : " + arrQRCode[2]);
			System.out.println("Token : " + arrQRCode[3]);*/
			
			/*2. Validate parse result*/
			
			if( flagQRCodeToken == 1 ){
				lValidate 			= customerService.validateQRCode( customerId,
																   	  email,
																   	  phoneNumber,
																   	  token);
			}else{
				lValidate 			= customerService.validateQRCode( customerId,
																   	  email,
																   	  phoneNumber);
			}
			
			
			if( Integer.parseInt( lValidate.get(0).toString() ) > 0 ){
				
				List<Object[]> lTransaction		= manager.createNativeQuery("CALL sp_transaction_model_1( ?, ?, ?, ?, ?, ?, ?, ?, ? )")
													.setParameter(1, customerId)
													.setParameter(2, userId)
													.setParameter(3, merchantTransactionNumber)
													.setParameter(4, totalPurchase)
													.setParameter(5, 0)
													.setParameter(6, 0)
													.setParameter(7, "")
													.setParameter(8, 1)
													.setParameter(9, dbKey)
													.getResultList();
				
				Iterator<Object[]> it 				= lTransaction.iterator();
				
				while( it.hasNext() ){
					
					Object[] result 				= (Object[])it.next();
					
					LOGGER.info("Length : " + result.length);
					LOGGER.info("### Result 0 : " + (String)result[0]);
					LOGGER.info("### Result 1 : " + (String)result[1]);
					LOGGER.info("### Result 2 : " + (String)result[2]);
					LOGGER.info("### Result 5 : " + (Double)result[5]);
					
					if( result.length == 6 ){
						statusSP 						= (String)result[0];
						msgSP 							= (String)result[1];
						transactionNumber 				= (String)result[2];
						lastBalance 					= (Double)result[5];
					}
					
				}
				
				
				
				if( !statusSP.equals("") ){
					
					Account acc 					= accService.getAccountDataByMemberId(customerId);
					
					//Get Merchant Info
					JSONObject joMerchantInfo 		= merchantUserService.getMerchantInfoByUserId(userId);
					
					// Add To Notification List
					JSONObject joNotification 		= notificationService.addToNotificationGetRebate(customerId, 
																									 "get_rebate", 
																							 		 (JSON.get(joMerchantInfo, "icon").equals("") ? "" : merchantPhotoPath + JSON.get(joMerchantInfo, "icon") ), 
																									 JSON.get(joMerchantInfo, "merchantName"), 
																									 JSON.get(joMerchantInfo, "merchantAddress"),
																									 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLatitude") ),
																									 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLongitude") ),
																									 lastBalance);	
					
					notificationId 					= JSON.getInt(joNotification, "notificationId");
					
					//Send notification to customer
					JSONObject joTransDetail 			= transDetailService.getTransactionDetailByTransactionNumber(transactionNumber, acc.getAccountId());
					
					Customer c							= customerService.getCustomerDetailByCustomerId(customerId);
					
					if( c.getFcmToken() != null && c.getFcmToken().compareTo("") != 0 ) {					
						JSONObject joPushNotification 	= fcmService.sendNotificationRebate(c.getFcmToken(), 
																							c.getCustomerId(), 
																							joTransDetail,
																							lastBalance,
																							notificationId);	
					}
					
					if( c.getApnToken() != null && c.getApnToken().compareTo("") != 0 ) {
						JSONObject joPushNotification 	= apnsService.sendNotificationRebate(c.getApnToken(), 
																					   	     c.getCustomerId(), 
																					   	     joTransDetail, 
																					   	     lastBalance,
																					   	     notificationId);
					}
					
					joResult.put("errCode", "00");
					joResult.put("errMsg", "Your transaction is successfull");
					joResult.put("transactionNumber", transactionNumber);
					
				}else{
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "Transaction failed");
					
				}
													
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Transaction failed, please use valid QRCode (01)");
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Transaction failed, please use valid QRCode (02)");
			LOGGER.info("[Transaction Model 1] " + e.getMessage());
		}
		
		
		return joResult;
		
	}
	
	
	@Transactional
	@Override
	public JSONObject transactionOnMerchantModel2( int userId,
												   String sessionToken,
												   JSONObject joParam ){
		
		JSONObject joResult		= new JSONObject();
		String qrCode 			= JSON.get(joParam, "qrCode");
		byte[] bQRCode 			= util.hexToByte( qrCode );
		String email			= "";
		long customerId 		= 0;
		String phoneNumber		= "";
		String token 			= "";
		
		String statusSP 		= "";
		String msgSP 			= "";
		String transactionNumber= "";
		
		double totalPurchase 		= Long.parseLong( JSON.get(joParam, "total") );
		double totalRebate 			= 0; 
//		double rebate 				= 0;
		Long merchantId 			= Long.parseLong( JSON.get(joParam, "merchantId") );
		BigInteger transactionId 	= null;
		
		List lValidate 				= null;
		
		long notificationId 		= 0;
		
		/*1. Validate QRCode*/
		//Format : email|customerId|phoneNumber|method
		//method => 1 : add via scan barcode on customer device 
		
		try {
			
			qrCode 					= (String)aes.decrypt(bQRCode, aesKey);
			String[] arrQRCode 		= qrCode.split("\\|");
			
			if( !arrQRCode[0].equals("") ){
				email			= arrQRCode[0];
			}
			if( !arrQRCode[1].equals("") ){
				customerId		= Long.parseLong( arrQRCode[1] );
			}
			if( !arrQRCode[2].equals("") ){
				phoneNumber		= arrQRCode[2];
			}
			
			if( flagQRCodeToken == 1 ){  
				if( !arrQRCode[3].equals("") ){
					token			= arrQRCode[3];
				}
			}
			
			System.out.println("Customer Id : " + customerId);
			
			/*2. Validate parse result*/
			if( flagQRCodeToken == 1 ){
				lValidate 			= customerService.validateQRCode( customerId,
																   	  email,
																   	  phoneNumber,
																   	  token);
			}else{
				lValidate 			= customerService.validateQRCode( customerId,
																   	  email,
																   	  phoneNumber);
			}
			
			if( Integer.parseInt( lValidate.get(0).toString() ) > 0 ){
				
				/*1. Calculate Total Purchase & Rebate*/
				JSONArray jaItem		= JSON.newJSONArray( JSON.get(joParam, "items").toString() );
				if( jaItem.size() > 0 ){
					
					for( int iItem = 0; iItem < jaItem.size(); iItem++ ){
						
						JSONObject joItem			= JSON.newJSONObject( jaItem.get(iItem).toString() );
						JSONObject joItemInfo   	= merchantItem.getMerchantItem(merchantId, 
																			   Long.parseLong( JSON.get(joItem, "itemId") ));
						
						totalRebate					+= ( Float.parseFloat( JSON.get(joItemInfo, "rebate") ) * Long.parseLong( JSON.get(joItem, "total") ) ) / 100;

					}
					
					/*2. Save to transaction*/
					List<Object[]> lTransaction		= manager.createNativeQuery("CALL sp_save_transaction_model_2(?,?,?,?,?)")
														.setParameter(1, customerId)
														.setParameter(2, userId)
														.setParameter(3, JSON.get(joParam, "merchantTransactionNumber"))
														.setParameter(4, totalPurchase)
														.setParameter(5, totalRebate)
														.getResultList();
					
					Iterator<Object[]> it 			= lTransaction.iterator();
					
					while( it.hasNext() ){
						
						Object[] result 				= (Object[])it.next();
						
						statusSP 						= (String)result[0];
						msgSP 							= (String)result[1];
						transactionNumber 				= (String)result[3];
						
						if( !statusSP.equals("") ){
							
							/*3. Save transaction to transaction detail*/
							
							transactionId 							= (BigInteger)result[2];
							
							List<Object[]> lTransactionDetail		= manager.createNativeQuery("CALL sp_transaction_model_2(?,?,?,?)")
																		.setParameter(1, customerId)
																		.setParameter(2, userId)
																		.setParameter(3, transactionId)
																		.setParameter(4, totalRebate)
																		.getResultList();
							
							Iterator<Object[]> itDetail 			= lTransaction.iterator();
							
							while( itDetail.hasNext() ){
								
								Object[] resultDetail				= (Object[])itDetail.next();
								
								statusSP 							= (String)resultDetail[0];
								msgSP 								= (String)resultDetail[1];
							
							}						
							
							/*Send notification to customer*/
							Account acc 					= accService.getAccountDataByMemberId(customerId);
							
							JSONObject joTransDetail 		= transDetailService.getTransactionDetailByTransactionNumber(transactionNumber, acc.getAccountId());
							
							Customer c						= customerService.getCustomerDetailByCustomerId(customerId);
							
							//Get Merchant Info
							JSONObject joMerchantInfo 		= merchantUserService.getMerchantInfoByUserId(userId);
							
							// Add To Notification List
							JSONObject joNotification 		= notificationService.addToNotificationGetRebate(customerId, 
																											 "get_rebate", 
																									 		 (JSON.get(joMerchantInfo, "icon").equals("") ? "" : merchantPhotoPath + JSON.get(joMerchantInfo, "icon") ), 
																											 JSON.get(joMerchantInfo, "merchantName"), 
																											 JSON.get(joMerchantInfo, "merchantAddress"),
																											 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLatitude") ),
																											 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLongitude") ),
																											 (double)0);	
							
							notificationId 					= JSON.getInt(joNotification, "notificationId");
							
							JSONObject joNotificationAndroid= fcmService.sendNotificationRebate(c.getFcmToken(), 
																						  		c.getCustomerId(), 
																						  		joTransDetail,
																						  		(double)0,
																						  		notificationId);
							
							joResult.put("errCode", "00");
							joResult.put("errMsg", msgSP);
							
						}else{
							
							joResult.put("errCode", statusSP);
							joResult.put("errMsg", msgSP);
							
						}
						
					}
					
				}else{
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "Transaction failed, please input item data");
					
				}
				
				/*2. Calculate Total Rebate */
				
				
													
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Transaction failed, please use valid QRCode");
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return joResult;
		
	}
	
	public JSONObject transactionRedeem( String voucherCode,
										 int userId,
										 String merchantTransactionNumber ) throws ParseException{
		
		JSONObject joResult 			= new JSONObject();
		boolean validateVoucherCode 	= false;
		String statusSP 				= "";
		String msgSP 					= "";
		String transactionNumber 		= "";
		String merchantName 			= "";
		double lastBalance 				= 0;
		
		/*1. Validate vouchercode first*/
		validateVoucherCode 			= eVoucherCustService.validateEVoucherCode(voucherCode, userId);
		
		if( validateVoucherCode ){
			
			 EVoucherCustomer lECode		= eVoucherCustService.findByVoucherCode(voucherCode);
			
			 List<Object[]> lRedeem			= manager.createNativeQuery( "CALL sp_redeem_voucher(?,?,?,?)" )
						 						.setParameter(1, voucherCode)
						 						.setParameter(2, lECode.getCustomerId())
						 						.setParameter(3, userId)
						 						.setParameter(4, merchantTransactionNumber)
						 						.getResultList();
			 
			 Iterator<Object[]> it 			= lRedeem.iterator();
			 
			 while( it.hasNext() ){
				 
				 Object[] result 		= (Object[])it.next();
				 
				 statusSP 				= (String)result[0];
				 msgSP 					= (String)result[1];
				 transactionNumber 		= (String)result[2];
				 merchantName 			= (String)result[3];
				 lastBalance			= (Double)result[4];
				 
			 }
			 
			 if( statusSP.equals("00") ){
				 
				 Customer c						= customerService.getCustomerDetailByCustomerId(lECode.getCustomerId());
				 
				 if( c.getShowVoucherNotification() == 1 ){
					 JSONObject joNotification 		= fcmService.sendNotificationRedeem(c.getFcmToken(), 
																						lECode.getCustomerId(), 
																						merchantName,
																						lastBalance,
																						lECode.getVoucherId());
				 }
				 
			 }
			 
			 joResult.put("errCode", statusSP);
			 joResult.put("errMsg", msgSP);
			 joResult.put("transactionNumber", transactionNumber);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg","Voucher code not valid.");
			
		}
		
		return joResult;
		
	}	
	
	@Transactional
	@Override
	public JSONObject transactionUsingCustomerApps( int customerId, 
													String merchantId,
													String cashierUsername,
													String cashierPassword,
													double total ) throws Exception {
		
		JSONObject joResult 			= new JSONObject();
		String decryptedMerchantId		= "";
		int cashierUserId 				= 0;
		String statusSP 				= "";
		String msgSP 					= "";
		String transactionNumber 	 	= "";
		double lastBalance 				= 0;
		long notificationId 			= 0;
		
		//1. Validate merchant id
		
		try {
			
//			System.out.println("### Merchant ID : " + merchantId);		
			decryptedMerchantId 			= aes.decrypt(merchantId, aesKey);
//			System.out.println("### DECODE MERCHANT ID  : " + decryptedMerchantId);
			
			if( accService.validateAccountNumber(decryptedMerchantId) ) {
				
				//2. Validate Cashier
				JSONObject joCashier 		= merchantService.authCashier(cashierUsername, 
																		  (encUtil.MD5Hashing( cashierPassword, dbMD5Key ) ).toUpperCase());
				
				if( JSON.get(joCashier, "errCode").equals("00") ) {
					
					cashierUserId 			= JSON.getInt(joCashier, "userId");
					
					//3. Calculate Rebate
					List<Object[]> lTransaction		= manager.createNativeQuery("CALL sp_transaction_model_1( ?, ?, ?, ?, ?, ?, ?, ?, ? )")
														.setParameter(1, customerId)
														.setParameter(2, cashierUserId)
														.setParameter(3, "")
														.setParameter(4, total)
														.setParameter(5, 0)
														.setParameter(6, 0)
														.setParameter(7, "")
														.setParameter(8, 1)
														.setParameter(9, dbKey)
														.getResultList();
					
					Iterator<Object[]> it 				= lTransaction.iterator();
					
					while( it.hasNext() ){
						
						Object[] result 				= (Object[])it.next();
						
						if( result.length == 6 ){
							statusSP 						= (String)result[0];
							msgSP 							= (String)result[1];
							transactionNumber 				= (String)result[2];
							lastBalance 					= (Double)result[5];
						}
						
					}
					
					if( !statusSP.equals("") ){
						
						Account acc 						= accService.getAccountDataByMemberId(customerId);
						
						//Get Merchant Info
						JSONObject joMerchantInfo 		= merchantUserService.getMerchantInfoByUserId(cashierUserId);
						
						// Add To Notification List
						JSONObject joNotification 		= notificationService.addToNotificationGetRebate(customerId, 
																										 "get_rebate", 
																								 		 (JSON.get(joMerchantInfo, "icon").equals("") ? "" : merchantPhotoPath + JSON.get(joMerchantInfo, "icon") ), 
																										 JSON.get(joMerchantInfo, "merchantName"), 
																										 JSON.get(joMerchantInfo, "merchantAddress"),
																										 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLatitude") ),
																										 Float.parseFloat( JSON.get(joMerchantInfo, "merchantLongitude") ),
																										 lastBalance);	
						
						notificationId 					= JSON.getInt(joNotification, "notificationId");
						
						//Send notification to customer
						JSONObject joTransDetail 			= transDetailService.getTransactionDetailByTransactionNumber(transactionNumber, acc.getAccountId());
						
						Customer c							= customerService.getCustomerDetailByCustomerId(customerId);
						
						if( c.getFcmToken() != null && c.getFcmToken().compareTo("") != 0 ) {					
							JSONObject joPushNotification 	= fcmService.sendNotificationRebate(c.getFcmToken(), 
																								c.getCustomerId(), 
																								joTransDetail,
																								lastBalance,
																								notificationId);	
						}
						
						if( c.getApnToken() != null && c.getApnToken().compareTo("") != 0 ) {
							JSONObject joPushNotification 	= apnsService.sendNotificationRebate(c.getApnToken(), 
																						   	     c.getCustomerId(), 
																						   	     joTransDetail, 
																						   	     lastBalance,
																						   	     notificationId);
						}
						
						joResult.put("errCode", "00");
						joResult.put("errMsg", "OK");
						joResult.put("transactionNumber", transactionNumber);
						
					}else {
						joResult.put("errCode", "-99");
						joResult.put("errMsg", "Transaction failed");
					}
					
				}else {
					joResult 				= joCashier;
				}
				
			}else {
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Merchant ID not found");
			}
			
		}catch( Exception ex ) {
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "QRCode not valid.");
			LOGGER.debug("Exception found : " + ex.getMessage());
			
		}
		
		return joResult;
		
	}
	
	@Transactional
	@Override
	public void getIncentiveAfterRegister( long customerId ) throws ParseException {
		
		long value1;
		long value2;
		
		/*1. Get incentive setting*/
		JSONObject joIncentive = incentiveService.getIncentiveSettingByIncentiveType("new_member");
		
		if( JSON.get(joIncentive, "errCode").equals("00") ) {
			JSONArray jaIncentiveData = JSON.newJSONArray(JSON.get(joIncentive, "data").toString());
			if( jaIncentiveData.size() > 0 ) {
				for( int i = 0; i < jaIncentiveData.size(); i++ ) {
					JSONObject joData = JSON.newJSONObject( jaIncentiveData.get(i).toString() );
					if( JSON.get(joData, "value1").equals("Add Free Rebate") ) {
						
						LOGGER.info("=============== Add Free Rebate =============== ");
						LOGGER.info(">>> Customer ID : " + customerId);
						LOGGER.info(">>> Amount Rebate : " + JSON.get(joData, "value2"));
						
						List<Object[]> lSPAddFreeRebate = manager.createNativeQuery("CALL sp_add_free_rebate(?,?,?,?)")
															.setParameter(1, customerId)
															.setParameter(2, Integer.parseInt(JSON.get(joData, "value2")))
															.setParameter(3, 0)
															.setParameter(4, -1)															
															.getResultList();
						/*List<Object[]> lSPAddFreeRebate = manager.createNativeQuery("CALL sp_add_free_rebate_test()")			
								.getResultList();*/
						
						Iterator<Object[]> it = lSPAddFreeRebate.iterator();
						while( it.hasNext() ) {
							
							Object[] result = (Object[])it.next();
							LOGGER.info(">>> Result SP [errCode] : " + (String)result[0]);
							LOGGER.info(">>> Result SP [errMsg] : " + (String)result[1]);
							LOGGER.info("================================================ ");
						}
						
					}
				}
			}
		}
		
	}
	
}
