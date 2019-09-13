package com.webservicemaster.iirs.service.customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.webservicemaster.iirs.dao.master.CustomerRepository;
import com.webservicemaster.iirs.domain.master.Account;
import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.domain.master.CustomerAccountBank;
import com.webservicemaster.iirs.domain.master.Friendship;
import com.webservicemaster.iirs.domain.master.SMSEmailQueue;
import com.webservicemaster.iirs.domain.master.Setting;
import com.webservicemaster.iirs.service.account.AccountService;
import com.webservicemaster.iirs.service.apnnotification.APNNotificationService;
import com.webservicemaster.iirs.service.bankcode.BankCodeService;
import com.webservicemaster.iirs.service.customeraccountbank.CustomerAccountBankService;
import com.webservicemaster.iirs.service.evoucher.EVoucherService;
import com.webservicemaster.iirs.service.fcmnotification.FCMNotificationService;
import com.webservicemaster.iirs.service.friendship.FriendshipService;
import com.webservicemaster.iirs.service.incentivesetting.IncentiveService;
import com.webservicemaster.iirs.service.notification.NotificationService;
import com.webservicemaster.iirs.service.oauth.OAuthService;
import com.webservicemaster.iirs.service.setting.SettingService;
import com.webservicemaster.iirs.service.smsemailqueue.SMSEmailService;
import com.webservicemaster.iirs.service.transaction.TransactionService;
import com.webservicemaster.iirs.service.transactiondetail.TransactionDetailService;
import com.webservicemaster.iirs.service.withdrawl.WithdrawlService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.EncryptorUtility;
import com.webservicemaster.iirs.utility.FormValidator;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.SessionIdGenerator;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	private CustomerRepository customerRepo;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Value("${db.key.md5}")
	private String dbMD5Key;
	
	@Value("${session.interval.timeout}")
	private int timeout;
	
	@Value("${link.verify}")
	private String masterVerifyLink;
	
	@Value("${aes.key}")
	private String aesKey;
	
	@Value("${db.key}")
	private String dbKey;
	
	@Value("${customer.photo.path}")
	private String customerPhotoPath;
	
	@Value("${customer.photo.url}")
	private String customerPhotoURL;
	
	@Value("${evoucher.photo.url}")
	private String evoucherPhotoURL;
	
	@Value("${photo.allowed.ext}")
	private String[] photoAllowedExt;
	
	@Value("${share.link.url}")
	private String shareLinkUrl;
	
	@Value("${url.tinyurl}")
	private String urlTinyURL;
	
	@Value("${ktp.photo.url}")
	private String ktpURL;
	
	@Value("${flag.qrcode.token}")
	private short flagQRCodeToken;
	
	@Autowired
	EncryptorUtility encUtil;
	
	@Autowired
	private Utility util;
	
	@Autowired
	SessionIdGenerator sessionGenerator;
	
	@Autowired
	private AES aes;
	
	@Autowired
	private AccountService accService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private SMSEmailService smsEmail;
	
	@Autowired
	private FriendshipService fService;
	
	@Autowired
	private FormValidator formValidator;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FCMNotificationService fcmService;
	
	@Autowired
	private APNNotificationService apnService;
	
	@Autowired
	private WithdrawlService withdrawlService;
	
	@Autowired
	private BankCodeService bankCodeService;
	
	@Autowired
	private CustomerAccountBankService customerAccountBankService;
	
	@Autowired
	private OAuthService oAuthService;
	
	@Autowired
	private EVoucherService eVoucherService;
	
	@Autowired
	private TransactionService transService;
	
	@Autowired
	CustomerServiceImpl(CustomerRepository repository) {
        this.customerRepo = repository;
    }
	
	@Override
	public JSONObject getMyProfile( long customerId ) throws Exception {
		
		JSONObject joData 				= new JSONObject();
		JSONObject joResult 			= new JSONObject();
		JSONObject joBankAccount 		= new JSONObject();
		
		String qrCode 					= "";
		String sessionToken 			= "";
		String shareLinkMessage 		= "";
		
		List<Object[]> lCustomer 		= customerRepo.getCustomerDetail(customerId);
		
		if( lCustomer.size() > 0 ){					
			
			Iterator<Object[]> it 	= lCustomer.iterator();
			
			while( it.hasNext() ){
				
				Object[] result 	= (Object[])it.next();
				
				if( (short)result[13] == 1 ){
					
					sessionToken 				= (String)result[5];
					
					if( flagQRCodeToken == 1 ){
						
						qrCode 					= aes.encrypt(( (String)result[3] + "|" + 
													    	(Long)result[0] + "|" + 
													    	(String)result[4] + "|" + 
													    	sessionToken ), 
													  	aesKey);
						
					}else{
						
						qrCode 					= aes.encrypt(( (String)result[3] + "|" + 
														    	(Long)result[0] + "|" + 
														    	(String)result[4] ), 
														  	aesKey);
						
					}
					
					// Get total Friend Request and Friend List
					List lTotalFR		= customerRepo.getTotalPendingRequest((Long)result[0]);
					List lTotalFL		= customerRepo.getTotalFriendList((Long)result[0]);
					Setting setting		= settingService.findBySettingPurposeAndSettingType("invite_content", 
																						    "sms");
					
					// Form ayares_id for referal
					shareLinkMessage 	= this.formInviteContent( (Long)result[0], (Long)result[6], (String)result[9], setting );
					
					// Get Bank Account Data
					CustomerAccountBank cba = customerAccountBankService.findByCustomerId((Long)result[0]);
					
					if( cba != null ){						
						joBankAccount.put("accountBankId", cba.getAccountBankId());
						joBankAccount.put("bankCode", cba.getBankCode());
						joBankAccount.put("bankName", cba.getBankName());
						joBankAccount.put("bankAccountNumber", cba.getBankAccountNumber());
						joBankAccount.put("bankAccountName", cba.getBankAccountName());
						joBankAccount.put("identityCard", cba.getIdentityCard());
						joBankAccount.put("fileIdentity", ( cba.getFileIdentity() == "" ? "" : ktpURL + "/" + cba.getFileIdentity() ) );						
					}
					
					joData.put("customerId", (Long)result[0]);
					joData.put("customerName", AES.decrypt( (String)result[1],dbKey ) );
					joData.put("userLogin", AES.decrypt( (String)result[2], dbKey ) );
					joData.put("email", AES.decrypt( (String)result[3], dbKey) );
					joData.put("customerPhoneNumber", AES.decrypt( (String)result[4], dbKey ) );
					joData.put("sessionToken", sessionToken);
					joData.put("accountId", (Long)result[6]);
					joData.put("qrCode", qrCode);
					joData.put("balance", (Double)result[7]);
					joData.put("photo", customerPhotoURL + (String)result[8]);
					joData.put("totalFriendRequest", Integer.parseInt( lTotalFR.get(0).toString()));
					joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString()));
					joData.put("accountNumber", (String)result[9]);	
					joData.put("inviteContent", shareLinkMessage);
					joData.put("showVoucherNotification", (short)result[10]);
					joData.put("showFriendRequestNotification", (short)result[11]);
					joData.put("fcmToken", (String)result[12]);
					joData.put("bankCode", bankCodeService.getBankCodeList());
					joData.put("firstTime", (short)result[14]);
					joData.put("bankAccountInfo", joBankAccount);
					
					joResult.put("errCode", "00");
					joResult.put("errMsg", "OK");
					joResult.put("data",joData);
					
				}else if( (short)result[13] == 0 ){
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "Your account is not active. Please verify your email.");
					
				}else if( (short)result[13] == -1 ){
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "Your account is blocked.");
					
				}
				
			}
			
		}else {
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Customer data not found.");
			
		}
		
		return joResult;
		
	}
	
	@Transactional
	@Override
	public JSONObject authLogin( String uLogin, 
								 String uPassword,
								 short isLoginViaMedsos,
								 String medsos,
								 String app ) throws Exception{
		
		LOGGER.debug("[authLogin] ### START (User Login : " + uLogin + ") ###");
		
		JSONObject joAuth 		 	= new JSONObject();
		JSONObject joData 		 	= new JSONObject();
		JSONObject joBankAccount 	= new JSONObject();
		String sessionToken      	= "";
		Date tokenExpired 		 	= null;
		String qrCode 			 	= "";
		String inviteContent	 	= "";
		String ayaresId 		 	= "";
		String simpleUrl 		 	= "";
		String shareLinkMessage  	= "";
		
		List<Object[]> lCustomer 	= null;
		
		LOGGER.info("User Login : " + AES.encrypt(uLogin, dbKey));
		LOGGER.info("User Password : " + encUtil.MD5Hashing( uPassword, dbMD5Key ).toUpperCase());
		
		LOGGER.debug("[authLogin] Encrypted User Login : " + AES.encrypt(uLogin, dbKey));
		LOGGER.debug("[authLogin] Encrypted User Password : " + encUtil.MD5Hashing( uPassword, dbMD5Key ).toUpperCase());
		
		LOGGER.debug("[authLogin] isLoginViaMedsos : " + isLoginViaMedsos);
		
		if( isLoginViaMedsos == 0 ) {
			lCustomer 				= customerRepo.authLogin( AES.encrypt(uLogin.toLowerCase(), dbKey), 
	   			   	  										  encUtil.MD5Hashing( uPassword, dbMD5Key ).toUpperCase() );
		}else if( isLoginViaMedsos == 1 ) {
			lCustomer 				= customerRepo.authLoginViaMedsos(AES.encrypt(uLogin.toLowerCase(), dbKey));
		}	
		
		LOGGER.debug("Customer Size : " + lCustomer.size());
		
		
		if( lCustomer.size() > 0 ){					
			
			Iterator<Object[]> it 	= lCustomer.iterator();
			
			while( it.hasNext() ){
				
				Object[] result 	= (Object[])it.next();
				
				if( result[15] != null && ((Date)result[15]).toString().compareTo("") != 0 ) {
				
					// Note : apabila result[15] null, error!!!!. Need Fixing.
					if( ((Date)result[15]).compareTo(util.getDateTime()) > 0 ){
						
						sessionToken	= (String)result[5];
						tokenExpired 	= util.addingMinutes(timeout);
						
					}else{
						sessionToken	= sessionGenerator.generateId(32);
						tokenExpired 	= util.addingMinutes(timeout);
					}
					
				}else {
					
					sessionToken	= sessionGenerator.generateId(32);
					tokenExpired 	= util.addingMinutes(timeout);
					
				}
				
				
				if( (short)result[13] == 1 ){
				
					// Set session Token
					customerRepo.setSessionToken(sessionToken, tokenExpired, (Long)result[0]);
					customerRepo.updateLoginInfo((short)1, 
												 util.localTransactionTime(), 
												 medsos,
												 (Long)result[0]);
					
					/*Version 1 : 
					 * Menggunakan session token.
					 * Dimana ada terjadi kasus token user tidak sama dengan yang di database, sehingga
					 * reject.*/
					/*qrCode 				= aes.encrypt(( (String)result[3] + "|" + 
													    (Long)result[0] + "|" + 
													    (String)result[4] + "|" + 
													    sessionToken ), 
													  aesKey);*/
					
					/*Version 2 : */					
					if( flagQRCodeToken == 1 ){
						
						qrCode 					= aes.encrypt(( (String)result[3] + "|" + 
													    	(Long)result[0] + "|" + 
													    	(String)result[4] + "|" + 
													    	sessionToken ), 
													  	aesKey);
						
					}else{
						
						qrCode 					= aes.encrypt(( (String)result[3] + "|" + 
														    	(Long)result[0] + "|" + 
														    	(String)result[4] ), 
														  	aesKey);
						
					}
					
					System.out.println("### QRCODE : " + qrCode);
					System.out.println("### RESULT[3] : " + (String)result[3]);
					
					
					// Get total Friend Request and Friend List
					List lTotalFR		= customerRepo.getTotalPendingRequest((Long)result[0]);
					List lTotalFL		= customerRepo.getTotalFriendList((Long)result[0]);
					Setting setting		= settingService.findBySettingPurposeAndSettingType("invite_content", 
																						    "sms");
					
					// Form ayares_id for referal
					shareLinkMessage 	= this.formInviteContent( (Long)result[0], (Long)result[6], (String)result[9], setting );
					
					// Get Bank Account Data
					CustomerAccountBank cba = customerAccountBankService.findByCustomerId((Long)result[0]);
					
					if( cba != null ){						
						joBankAccount.put("accountBankId", cba.getAccountBankId());
						joBankAccount.put("bankCode", cba.getBankCode());
						joBankAccount.put("bankName", cba.getBankName());
						joBankAccount.put("bankAccountNumber", cba.getBankAccountNumber());
						joBankAccount.put("bankAccountName", cba.getBankAccountName());
						joBankAccount.put("identityCard", cba.getIdentityCard());
						joBankAccount.put("fileIdentity", ( cba.getFileIdentity() == "" ? "" : ktpURL + "/" + cba.getFileIdentity() ) );						
					}
					
					joData.put("customerId", (Long)result[0]);
					joData.put("customerName", AES.decrypt( (String)result[1],dbKey ) );
					
					if( result[2]!= null && ((String)result[2]).compareTo("") != 0 ) {
						joData.put("userLogin", AES.decrypt( (String)result[2], dbKey ) );
					}	
						
					joData.put("email", AES.decrypt( (String)result[3], dbKey) );
					
					if( result[4]!= null && ((String)result[4]).compareTo("") != 0 ) {
						joData.put("customerPhoneNumber", AES.decrypt( (String)result[4], dbKey ) );
					}	
					joData.put("sessionToken", sessionToken);
					joData.put("accountId", (Long)result[6]);
					joData.put("qrCode", qrCode);
					joData.put("balance", (Double)result[7]);
					joData.put("photo", customerPhotoURL + (String)result[8]);
					joData.put("totalFriendRequest", Integer.parseInt( lTotalFR.get(0).toString()));
					joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString()));
					joData.put("accountNumber", (String)result[9]);	
					joData.put("inviteContent", shareLinkMessage);
					joData.put("showVoucherNotification", (short)result[10]);
					joData.put("showFriendRequestNotification", (short)result[11]);
					joData.put("fcmToken", (String)result[12]);
					joData.put("bankCode", bankCodeService.getBankCodeList());
					joData.put("firstTime", (short)result[14]);
					joData.put("bankAccountInfo", joBankAccount);
					
					joAuth.put("errCode", "00");
					joAuth.put("errMsg", "OK");
					joAuth.put("data",joData);
					
					/*Set first login*/
					customerRepo.updateFirstLogin((Long)result[0]);
					
				}else if( (short)result[13] == 0 ){
					
					joAuth.put("errCode", "-99");
					joAuth.put("errMsg", "Your account is not active. Please verify your email.");
					
				}else if( (short)result[13] == -1 ){
					
					joAuth.put("errCode", "-99");
					joAuth.put("errMsg", "Your account is blocked.");
					
				}
				
			}			
			
		}else{
			
			joAuth.put("errCode", "-99");
			joAuth.put("errMsg", "Login failed");
			
		}
		
		LOGGER.debug("[authLogin] ### END (User Login : " + uLogin + ") ###");
		
		return joAuth;
	}

	@Transactional
	@Override
	public JSONObject register( String customerName,
							    String userLogin,
							    String userPassword,
							    String email,
							    String customerPhoneNumber,
							    String customerPhoto,
							    String app,							    
							    short acceptTermCondition,
							    short referalMode,
							    String referalNumber,
							    short loginViaMedsos,
							    String medsos,
							    String medsosId) throws Exception{
		

		
		JSONObject jo 				= new JSONObject();
		long accountId 				= 0;
		long customerId 			= 0;
		String subject 				= "";
		String body 				= "";
		String verifyCode 			= "";
		String verifyLink 			= this.masterVerifyLink;
		
		String[] validateElement	= new String[]{"name","username", "password", "email", "phoneNumber"};
		String[] validateValue 		= new String[]{customerName, userLogin, userPassword, email, customerPhoneNumber};
		JSONArray jaElement 		= null;
		JSONObject joValidateResult = null;
		
		List lCheckEmail 			= null;
		List lCheckPhoneNumber 		= null;
		List lCheckUserLogin 		= null;
		
		boolean flagRegister 		= false;
		
		/*0. Check Email Exists*/
		if( !email.equals("") ) {
			lCheckEmail 		= customerRepo.isEmailExist(AES.encrypt(email.toLowerCase(), dbKey));
		}
		
		if( !customerPhoneNumber.equals("") ) {
			lCheckPhoneNumber	= customerRepo.isPhoneNumberExists(AES.encrypt(customerPhoneNumber, dbKey));
		}
		
		if( !userLogin.equals("") ) {
			
			/*Check is there any validation voucher based on prefix of user login*/
			/*String[] arrUserlogin = userLogin.toLowerCase().split("_");
			JSONObject joVoucher = eVoucherService.getEVoucher((short)1, arrUserlogin[0]);
			LOGGER.info("--> Userlogin : " + arrUserlogin[0]);
			if( JSON.get(joVoucher, "errCode").equals("00") ) {
				LOGGER.info("-->Auto Campaign");
			}else{
				LOGGER.info("-->Not Auto Campaign");
			}*/
			
			lCheckUserLogin 	= customerRepo.isUserLoginExists( AES.encrypt(userLogin.toLowerCase(), dbKey) );
		}
		
		if( loginViaMedsos == 0 ) {
			
			jaElement 				= formValidator.createCustomerRegistrationValidation(validateElement, validateValue);
			joValidateResult 		= formValidator.validateElement(jaElement);	
			
			if( JSON.get(joValidateResult, "errCode").equals("00") ) {	
				
				if( Integer.parseInt( lCheckEmail.get(0).toString() ) > 0 ){
					
					if( joValidateResult.containsKey("email") ) {							
						joValidateResult.put("email", "Email already registered. Please use another email.");
					}
					
					flagRegister = false;
					
				}/*else if( Integer.parseInt( lCheckPhoneNumber.get(0).toString() ) > 0 ){
					
					jo.put("errCode", "-99");
					jo.put("errMsg", "Phone number has registered before. Please use another phone number.");
					
				}*/else if( lCheckUserLogin != null && Integer.parseInt( lCheckUserLogin.get(0).toString() ) > 0 ){
					
					if( joValidateResult.containsKey("username") ) {							
						joValidateResult.put("username", "Username already registered. Please use another Username.");
					}
					
					flagRegister = false;
					
				}else {
					flagRegister = true;
				}			
				
			
			}else {
				flagRegister = false;
			}
			
		}else {
			
			if( !email.equals("") ) {
				
				if( Integer.parseInt( lCheckEmail.get(0).toString() ) > 0 ){
					flagRegister = false;		
				}else {
					flagRegister = true;
				}
				
			}else {
				//Check apakah medsosId sudah ada atau belum berdasarkan tipe medsos
				List lValidateMedsosId 	= customerRepo.validateMedsosId(medsosId, medsosId, medsosId);
				
				if( Integer.parseInt( lValidateMedsosId.get(0).toString() ) > 0 ) {			
					flagRegister = false;				
				}else {
					flagRegister = true;
				}
			}		
			
		}
		
		if( flagRegister ){
		
			/*2. Add to ms_customers*/
			
			String encPassword	= encUtil.MD5Hashing(userPassword, dbMD5Key).toUpperCase();
			
			Customer c 			= new Customer();
			
			if( loginViaMedsos == 0 ) {
				c.setCustomerName( AES.encrypt( customerName, dbKey ) );
				c.setUserLogin( AES.encrypt( userLogin.toLowerCase(), dbKey) );
				c.setUserPassword( encPassword );
				c.setEmail( AES.encrypt( email.toLowerCase(), dbKey ) );
				c.setCustomerPhoneNumber( AES.encrypt( customerPhoneNumber, dbKey ) );
				c.setRegisterDate( util.localTransactionTime() );
				//Note : semetara aktif dlu
				c.setStatus( (short)0 );
				c.setFirstLogin((short)1);
				
//				c.setStatus((short)1);
				c.setAcceptTermCondition(acceptTermCondition);
				c.setCustomerPhoto(customerPhoto);
				c.setShowFriendRequestNotification( (short)1 );
				c.setShowVoucherNotification( (short)1 );
				
			}else if( loginViaMedsos == 1 ) {
				
				c.setCustomerName( AES.encrypt( customerName, dbKey ) );
				c.setEmail( AES.encrypt( email.toLowerCase(), dbKey ) );
				if( !customerPhoneNumber.equals("") ) {
					c.setCustomerPhoneNumber( AES.encrypt( customerPhoneNumber, dbKey ) );
				}
				c.setRegisterDate( util.localTransactionTime() );
				c.setStatus( (short)1 );
				c.setFirstLogin((short)1);
				c.setAcceptTermCondition((short)1);
				c.setCustomerPhoto(customerPhoto);
				c.setShowFriendRequestNotification( (short)1 );
				c.setShowVoucherNotification( (short)1 );
				
				if( medsos.equals("fb") ) {
					c.setFbId( medsosId );
				}else if( medsos.equals("gmail") ) {
					c.setGmailId( medsosId );
				}else if( medsos.equals("twitter") ) {
					c.setTwitterId(medsosId);
				}
				
			}
			
			customerRepo.save(c);
			
			
			if( c.getCustomerId() > 0 ){
				
				customerId = c.getCustomerId();
				
				/*3. Generate Account Number*/
				JSONObject joAccount	= accService.addAccount("customer", c.getCustomerId());
				
				if( JSON.get(joAccount, "errCode").equals("00") ){			
					
					/*4. Send verify email*/				
					//Generate verify code
					verifyCode 			= userLogin.toLowerCase() + "|" + 
										  email + "|" + 
										  customerPhoneNumber + "|" + 
										  c.getCustomerId();
					
					verifyCode 			= aes.encrypt(verifyCode, aesKey);
					verifyLink 			= verifyLink.replace("[#hashCode#]", verifyCode);
					
					List<Setting> lSetting		= settingService.findBySettingPurpose("registration");
					for( Setting dSetting : lSetting ){
						
						if( dSetting.getSettingId() == 11 ){						
							subject 			= dSetting.getValue();						
						}else if( dSetting.getSettingId() == 10 ){
							body 				= dSetting.getValue();
						}
						
					}
					
					body 					= body.replace("[#full_name#]", customerName);
					body 					= body.replace("[#verify_link#]", verifyLink);
					
					sendNotification( body, subject, email, "email" );
					LOGGER.info("[register] Referal Mode : " + referalMode);
					LOGGER.info("[register] Referal Number : " + referalNumber);
					
					/*if( referalNumber.compareTo("") != 0 ){
						
						String decryptedReferalNumber2 = aes.decrypt(referalNumber, aesKey);	
						String[] arrReferalNumber2 	  = decryptedReferalNumber2.split("\\|");
						LOGGER.info("[register] Referal Number : " + arrReferalNumber2[2]);
					}*/
					
					/*5. If referalMode = 1*/
					if( referalMode == 1 && referalNumber.compareTo("utm_source=google-play&utm_medium=organic") != 0 ){
						
						String decryptedReferalNumber = aes.decrypt(referalNumber, aesKey);	
						String[] arrReferalNumber 	  = decryptedReferalNumber.split("\\|");						
						
						JSONObject joReferalResult = inviteFriendUsingReferalId(arrReferalNumber[2], c.getCustomerId());
						LOGGER.info("[register] Referal Result : " + joReferalResult.toString());
						
					}else{
						//6. If download from google play (referalMode = 0), then invitator is company
						fService.inviteByCompany(c.getCustomerId());
					}
					
					/*7. Automatic friend with 3ofus*/
					/*LOGGER.info("Customer ID : " + c.getCustomerId());
					this.friend3OfUs(c.getCustomerId());*/						
					
					jo.put("errCode", "00");
					jo.put("errMsg", "You have successfully registration. Please verify your email.");
					jo.put("customerId", customerId);
					
					/*Note : 
					 * This is special rules for founder of AYARES.
					 * When people register for first time, automatically they are friend's of us.*/
					
					
				   	
					
				}else{
					
					jo 					= joAccount;
					
				}
				
			
				
				
			}			
						
			
		}else{
			
			if( loginViaMedsos == 0 && !joValidateResult.isEmpty() ) {
				
				if( jo.isEmpty() ) {
					
					if( flagRegister ) {
						jo.put("errCode", JSON.get(joValidateResult, "errCode"));
						jo.put("errMsg", JSON.get(joValidateResult, "errMsg"));
						
					}else {
						jo.put("errCode", "-99");
						jo.put("errMsg", "Parameter not valid");
					}
					
				}
				
				jo.put("errField", joValidateResult);
				
			}else {
				jo.put("errCode", "00");
				jo.put("errMsg", "Validate for login via medsos successfully");
			}
			
			
		}
			
		
		
		return jo;		
	}
	
	
	@Override
	@Transactional
	public JSONObject forgotPassword( String email ) throws Exception{
		
		JSONObject joResult			= new JSONObject();
		String clearPassword		= "";
		String encPassword 			= "";
		String subject 				= "";
		String body 				= "";
		String customerName 		= "";
		String username 			= "";
		
		String[] validateElement	= new String[]{"email"};
		String[] validateValue 		= new String[]{email};
		JSONArray jaElement 		= null;
		JSONObject joValidateResult = null;
		
		/*0. Validate format email*/
		jaElement 				= formValidator.createCustomerRegistrationValidation(validateElement, validateValue);
		joValidateResult 		= formValidator.validateElement(jaElement);		

		if( JSON.get(joValidateResult, "errCode").compareTo("00") == 0 ){

			/*1. Check Email*/
			List lCheckEmail 		= customerRepo.isEmailExist(AES.encrypt(email, dbKey));
			
			if( Integer.parseInt( lCheckEmail.get(0).toString() ) > 0 ){
				
				/*2. Reset password*/
				clearPassword 		= util.generateRandom(8);
				encPassword			= encUtil.MD5Hashing(clearPassword, dbMD5Key).toUpperCase();
				
				/*3. Update to database*/
				int updateResult 	= customerRepo.changePassword(encPassword, AES.encrypt(email, dbKey));
				
				if( updateResult > 0 ){
					
					List<Setting> lSetting		= settingService.findBySettingPurpose("forgot_password");
					for( Setting dSetting : lSetting ){
						
						if( dSetting.getSettingId() == 7 ){						
							body 				= dSetting.getValue();						
						}else if( dSetting.getSettingId() == 12 ){
							subject  			= dSetting.getValue();
						}
						
					}
					
					List<Customer> lCustomer	= customerRepo.findByEmail( AES.encrypt(email,dbKey));
					
					for( Customer cData : lCustomer ){
						customerName			= cData.getCustomerName();
						username 				= cData.getUserLogin();
					}
					
					body 						= body.replace("[#member_name#]", AES.decrypt(customerName,dbKey));
					body 						= body.replace("[#new_password#]", clearPassword);
					body 						= body.replace("[#username#]", AES.decrypt(username,dbKey));
					
					sendNotification(body, subject, email, "email");
					
					joResult.put("errCode", "00");
					joResult.put("errMsg", "An email has been sent to the User with new password.");
					
					// Sementara untuk development
					joResult.put("newPassword", clearPassword);
					
				}
				
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "The email you entered is not registered. Please use registered email.");
				
			}
			
		}else{
			joResult.put("errCode", "-98");
			joResult.put("errMsg", "Field not valid");
			joResult.put("errField", joValidateResult);
		}
		
		return joResult;
		
	}
	
	@Transactional
	@Override
	public JSONObject updatePassword( long customerId,
									  String currentPassword,
									  String newPassword ){
		
		JSONObject jo		= new JSONObject();
		String encPassword	= "";
		int updateStatus 	= 0;
		
		/*1. Check current password*/
		encPassword  		= encUtil.MD5Hashing(currentPassword, dbMD5Key).toUpperCase();
		List lCheck 		= customerRepo.checkCurrentPassword(customerId, encPassword);
		
		if( Integer.parseInt( lCheck.get(0).toString() ) > 0 ){
			
			/*2. Encrypt new password and update to database*/
			encPassword 	= encUtil.MD5Hashing(newPassword, dbMD5Key);
			updateStatus 	= customerRepo.changePassword(encPassword, customerId);
			
			if( updateStatus > 0 ){
				
				jo.put("errCode", "00");
				jo.put("errMsg", "You've successfully update password.");
				
			}else{
				
				jo.put("errCode", "-99");
				jo.put("errMsg", "Update password failed, please try again later.");
				
			}
			
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Your current password not valid.");
			
		}
		
		return jo;
		
	}
	
	@Transactional
	@Override
	public JSONObject updateProfile( long customerId,
									String customerName,
									String userLogin,
									String email,
									String customerPhoneNumber,
									String customerPhoto) throws Exception{
		
		JSONObject jo				= new JSONObject();
		String encPassword			= "";
		int updateStatus 			= 0;
		JSONArray jaElement 		= null;
		JSONObject joValidateResult = null;
		
		/*1. Check customer id available or not*/
		List lValCust		= customerRepo.validateByCustomerId(customerId);
		
		if( Integer.parseInt( lValCust.get(0).toString() ) > 0 ){
			
			/*2. validate email, phone number for updaate*/
			List lValEmail			= customerRepo.validateEmailForUpdate( aes.encrypt(email.toLowerCase(), dbKey), customerId);
			List lValPhone			= customerRepo.validatePhoneNumberForUpdate(aes.encrypt(customerPhoneNumber,dbKey), customerId);
			List lCheckUserLogin 	= customerRepo.isUserLoginExistsForUpdate( AES.encrypt(userLogin.toLowerCase(), dbKey), customerId );
			
			if( Integer.parseInt( lValPhone.get(0).toString() ) > 0 ){
				jo.put("errCode", "-99");
				jo.put("errMsg", "Phone number has been registered before. Please use another phone number");
			}else if( Integer.parseInt( lValEmail.get(0).toString() ) > 0 ){
				jo.put("errCode", "-99");
				jo.put("errMsg", "Email has been registered before. Please use another email");
			}else if( Integer.parseInt( lCheckUserLogin.get(0).toString() ) > 0 ){
				
				jo.put("errCode", "-99");
				jo.put("errMsg", "Username has registered before. Please use another Username.");
				
			}else{
				
				/*3. Validate form element*/
				String[] validateElement	= new String[]{"name","username", "email", "phoneNumber"};
				String[] validateValue 		= new String[]{customerName, userLogin, email, customerPhoneNumber};
				
				jaElement 					= formValidator.createCustomerRegistrationValidation(validateElement, validateValue);
				joValidateResult 			= formValidator.validateElement(jaElement);		
				
				if( JSON.get(joValidateResult, "errCode").compareTo("00") == 0 ){
				
					updateStatus 		= customerRepo.updateProfile( AES.encrypt( customerName, dbKey ), 
																	  AES.encrypt( userLogin.toLowerCase(), dbKey), 
																	  AES.encrypt( email.toLowerCase(), dbKey), 
																	  AES.encrypt( customerPhoneNumber, dbKey ), 
																     customerPhoto,
																     customerId);
					
					if( updateStatus > 0 ){
					
						jo.put("errCode", "00");
						jo.put("errMsg", "Update profile successfully");
					
					}else{
					
						jo.put("errCode", "-99");
						jo.put("errMsg", "Update profile failed.");
					
					}
					
				}else{
					
					jo.put("errCode", "-98");
					jo.put("errMsg", "Field not valid");
					jo.put("errField", joValidateResult);
					
				}
				
			}
						
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Update failed. Please using valid data to verify.");
			
		}	
		
		
		return jo;
		
	}	
	
	@Transactional
	@Override
	public JSONObject inviteFriend( long customerId,
									String qrCode ){
		
		JSONObject jo 				= new JSONObject();
		long friendMemberId			= 0;
		String email 				= "";
		String phoneNumber 			= "";
		String token 				= "";
		String decryptedCustName	= "";
			
		try {
			
			byte[] bQRCode 		= util.hexToByte( qrCode );
			qrCode 				= (String)aes.decrypt(bQRCode, aesKey);
			String[] arrQRCode 	= qrCode.split("\\|");
			String method 		= "";
			List lValidate 		= null;
			
			//1. Parse QRCode
			//Format : email|friendMemberId|phoneNumber|token
			//Invitator always give the QRCode
			//method => 1 : add via scan barcode on friend device 
			//method => 2 : add via invitator (share using socmed, sms etc)
			
			if( !arrQRCode[0].equals("") ){
				email			= arrQRCode[0];
			}
			if( !arrQRCode[1].equals("") ){
				friendMemberId	= Long.parseLong( arrQRCode[1] );
			}
			if( !arrQRCode[2].equals("") ){
				phoneNumber		= arrQRCode[2];
			}
			
			if( flagQRCodeToken == 1 ){
				if( !arrQRCode[3].equals("") ){
					token 			= arrQRCode[3];
				}
			}
			
			method 			= "1";
			
			
			//2. Validate parse result
			if( flagQRCodeToken == 1 ){
				lValidate 		= customerRepo.validateQRCode( friendMemberId,
					  										   email,
					  										   phoneNumber,
					  										   token);
			}else{
				
				lValidate 		= customerRepo.validateQRCode( friendMemberId,
															   email,
															   phoneNumber);
				
			}
			
			if( Integer.parseInt( lValidate.get(0).toString() ) > 0 ){
				
				//3. Validate is invite before
				if( fService.checkFriendshipStatus(friendMemberId, customerId) ){
					
					//4. Validate is invited by other person
					if( fService.checkIsInvitedWithSomeone(customerId) ){
						
						Friendship f 	= new Friendship();
						f.setInviteDateTime( util.getDateTime() );
						f.setInvitatorId( friendMemberId );
						f.setInvitedId( customerId );
						f.setType( "just_friend" );
						f.setStatus((short)1);
						f.setInvitationMethod( Short.parseShort( method )  );
						
						jo				= fService.inviteFriendViaQRCode(f);	
						
					}else{
						
						Friendship f 	= new Friendship();
						f.setInviteDateTime( util.getDateTime() );
						f.setInvitatorId( friendMemberId );
						f.setInvitedId( customerId );
						f.setType( "invited" );
						f.setStatus((short)1);
						f.setInvitationMethod( Short.parseShort( method )  );
						
						
						jo					= fService.inviteFriendViaQRCode(f);						
						
					}
					
					/*Request By Pak Edy : 
					 * Get friendName and friendImageURL*/
					Customer fDetail	= customerRepo.findByCustomerId(friendMemberId);
					Customer cDetail	= customerRepo.findByCustomerId(customerId);
					jo.put("friendName", AES.decrypt( fDetail.getCustomerName() , dbKey));
					if( fDetail.getCustomerPhoto().compareTo("") != 0 ){
						jo.put("friendImageURL", customerPhotoURL + fDetail.getCustomerPhoto());
					}else{
						jo.put("friendImageURL","");
					}
					
					decryptedCustName 			  = aes.decrypt( cDetail.getCustomerName(), dbKey);
					
					if( fDetail.getShowFriendRequestNotification() == 1 ){
						
						/*Add to notification*/
						JSONObject joNotification = notificationService.addToNotificationSendFriendRequest(friendMemberId, 
																										   ( cDetail.getCustomerPhoto().equals("") ? "" : ( customerPhotoURL + cDetail.getCustomerPhoto() ) ), 
																										   decryptedCustName);
						
						if( fDetail.getFcmToken() != null && !fDetail.getFcmToken().equals("") ) {
							/*Send notification*/
							JSONObject joNotificationMobile = fcmService.sendNotificationFriendRequest(fDetail.getFcmToken(), 
																	 							 fDetail.getCustomerId(), 
																	 							 decryptedCustName, 
																	 							 cDetail.getCustomerPhoto(),
																	 							 (short)0,
																	 							 Long.parseLong(JSON.get(joNotification, "notificationId")));
							LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
						}
						
						if( fDetail.getApnToken() != null && !fDetail.getApnToken().equals("") ) {
							/*Send notification*/
							JSONObject joNotificationMobile = apnService.sendNotificationFriendRequest(fDetail.getApnToken(), 
																	 							 		fDetail.getCustomerId(), 
																	 							 		decryptedCustName, 
																	 							 		cDetail.getCustomerPhoto(),
																	 							 		(short)0,
																	 							 		Long.parseLong(JSON.get(joNotification, "notificationId")));
							LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
						}
						
						
					}
					
					
				
				}else{
					
					jo.put("errCode", "-99");
					jo.put("errMsg", "You've friendship this person before");
					
				}
							
				
			}else{
				
				jo.put("errCode", "-99");
				jo.put("errMsg", "The QRCode not registered on our system");
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jo;
		
	}
	
	/*Invite friend via QRCode that comes from account number*/
	/*@Transactional
	@Override
	public JSONObject inviteFriend( long customerId,
									String qrCode ){
		
		JSONObject jo 		= new JSONObject();
		String email 		= "";
		String phoneNumber 	= "";
			
		try {
			
			byte[] bQRCode 		= util.hexToByte( qrCode );
			qrCode 				= (String)aes.decrypt(bQRCode, aesKey);
			String[] arrQRCode 	= qrCode.split("\\|");
			String method 		= "";
			
			//method => 1 : add via scan barcode on friend device 
			//method => 2 : add via invitator (share using socmed, sms etc)			
			
			1. Validate QRCode
			List<Customer> lValidate 		= customerRepo.validateQRCode( qrCode );
			
			if( lValidate.size() > 0 ){
			
				for( Customer c : lValidate ){
						
					3. Validate is invite before
					if( fService.checkFriendshipStatus(c.getCustomerId(), customerId) ){
						
						4. Validate is invited by other person
						if( fService.checkIsInvitedWithSomeone(customerId) ){
							
							Friendship f 	= new Friendship();
							f.setInviteDateTime( util.getDateTime() );
							f.setInvitatorId( c.getCustomerId() );
							f.setInvitedId( customerId );
							f.setType( "just_friend" );
							f.setStatus((short)0);
							f.setInvitationMethod( (short)1  );
							
							jo				= fService.inviteFriendViaQRCode(f);	
							
						}else{
							
							Friendship f 	= new Friendship();
							f.setInviteDateTime( util.getDateTime() );
							f.setInvitatorId( c.getCustomerId() );
							f.setInvitedId( customerId );
							f.setType( "invited" );
							f.setStatus((short)0);
							f.setInvitationMethod( (short)1 );
							
							jo				= fService.inviteFriendViaQRCode(f);	
							
						}						
						
						
					}else{
						
						jo.put("errCode", "-99");
						jo.put("errMsg", "You've friendship this person before");
						
					}
					
					
				}
				
			}else{	
				jo.put("errCode", "-99");
				jo.put("errMsg", "The QRCode not registered on our system");			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jo;
		
	}*/
	
	@Override
	@Transactional
	public JSONObject inviteJustFriend( JSONArray arrPhoneNumber,
										long customerId ) throws Exception{
		
		JSONObject jo 				= new JSONObject();
		String phoneNumber 			= "";
		long friendId 				= 0;
		String decryptedCustName 	= "";
		
		JSONArray jaData 		  	= new JSONArray();
		JSONObject joData 		  	= null;
		JSONObject joInviteStatus 	= null;
		
		for( int i=0; i<arrPhoneNumber.size(); i++ ){
			
			phoneNumber 				= "";
			phoneNumber 				= arrPhoneNumber.get(i).toString();
			joData 						= new JSONObject();
			joInviteStatus 				= new JSONObject();
			
			/*1. Get customerId by phoneNumber*/
			Customer c 					= customerRepo.findByCustomerPhoneNumber( aes.encrypt( phoneNumber, dbKey ) );
			friendId					= c.getCustomerId();
			
			//Sebelumnya menggunakan List<Customer>
			
			if( friendId != 0 ){
				
				if( fService.checkFriendshipStatus(friendId, customerId) ){
				
					Friendship f 	= new Friendship();
					f.setInviteDateTime( util.getDateTime() );
					f.setInvitatorId( customerId );
					f.setInvitedId( friendId );
					f.setType( "just_friend" );
					f.setStatus((short)0);
					f.setInvitationMethod( (short)3  );
					
					joInviteStatus			= fService.inviteFriendViaQRCode(f);
					
					joData.put("phoneNumber", phoneNumber);
					joData.put("inviteStatus", "00");
					joData.put("inviteMsg", "OK");
					jaData.add(joData);
					
					Customer fDetail		= customerRepo.findByCustomerId(friendId);
					Customer cDetail		= customerRepo.findByCustomerId(customerId);
					
					decryptedCustName 		= aes.decrypt( cDetail.getCustomerName(), dbKey);

					if( fDetail.getShowFriendRequestNotification() == 1 ){
						
						/*Add to notification*/
						JSONObject joNotification = notificationService.addToNotificationSendFriendRequest(friendId, 
																										   ( cDetail.getCustomerPhoto().equals("") ? "" : ( customerPhotoURL + cDetail.getCustomerPhoto() ) ), 
																										   decryptedCustName);
						
						if( fDetail.getFcmToken() != null && !fDetail.getFcmToken().equals("") ) {
							/*Send notification*/
							JSONObject joNotificationMobile = fcmService.sendNotificationFriendRequest(fDetail.getFcmToken(), 
																										 fDetail.getCustomerId(), 
																										 decryptedCustName, 
																										 cDetail.getCustomerPhoto(),
																										 (short)1,
																										 Long.parseLong(JSON.get(joNotification, "notificationId")));
							
							LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
						
						}
						
						if( fDetail.getApnToken() != null && !fDetail.getApnToken().equals("") ) {
							/*Send notification*/
							JSONObject joNotificationMobile = apnService.sendNotificationFriendRequest(fDetail.getApnToken(), 
																										 fDetail.getCustomerId(), 
																										 decryptedCustName, 
																										 cDetail.getCustomerPhoto(),
																										 (short)1,
																										 Long.parseLong(JSON.get(joNotification, "notificationId")));
							LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
						}
	
						
					}
					
				}else{
					
					joData.put("phoneNumber", phoneNumber);
					joData.put("inviteStatus", "-99");
					joData.put("inviteMsg", "Already invited");
					jaData.add(joData);
					
				}
				
			}else{
				
				joData.put("phoneNumber", phoneNumber);
				joData.put("inviteStatus", "-99");
				joData.put("inviteMsg","Phone Number not registered");
				jaData.add(joData);
				
			}
			
		}
		
		jo.put("errCode", "00");
		jo.put("errMsg", "OK");
		jo.put("data", jaData);
		
		return jo;
		
	}
	
	
	@Transactional
	@Override
	public JSONObject confirmFriendship( long customerId,
										 short status, 
										 short isBlockFutureInvitation ,
										 long friendId ) throws ParseException, Exception{
		
		JSONObject jo										= new JSONObject();
		long friendCustomerId								= 0;
		String decryptedCustName, decryptedInvitatorName	= "";
		
		/*0. Get Customer Id*/
		Friendship f 				= fService.findByFriendId(friendId);
		
		/*1. Check friendship*/
		if( fService.checkFriendShipForConfirm(friendId, customerId) ){
			
			jo	 					= fService.confirmFriendship(status, isBlockFutureInvitation, friendId);
			
			/*Send notification to friend*/
			Customer fDetail 		= customerRepo.findByCustomerId(f.getInvitatorId()); 
			decryptedInvitatorName	= aes.decrypt(fDetail.getCustomerName(), dbKey);
						
			if( fDetail.getShowFriendRequestNotification() == 1 ){
				
				Customer cDetail 	= customerRepo.findByCustomerId(customerId);
				decryptedCustName 	= aes.decrypt( cDetail.getCustomerName(), dbKey);
				
				/*Send notification*/
				/*JSONObject joNotification = fcmService.sendAcceptNotificationFriendRequest(fDetail.getFcmToken(), 
																					 	   fDetail.getCustomerId(), 
																					 	   aes.decrypt( cDetail.getCustomerName(), dbKey), 
																					 	   cDetail.getCustomerPhoto());*/
				
				/*Add to notification*/
				JSONObject joNotification = notificationService.addToNotificationAcceptFriendRequest(fDetail.getCustomerId(), 
																								     ( cDetail.getCustomerPhoto().equals("") ? "" : ( customerPhotoURL + cDetail.getCustomerPhoto() ) ), 
																								     decryptedCustName);
				
				if( fDetail.getFcmToken() != null && !fDetail.getFcmToken().equals("") ) {
					/*Send notification*/
					/*JSONObject joNotificationMobile = fcmService.sendNotificationFriendRequest(fDetail.getFcmToken(), 
																								 fDetail.getCustomerId(), 
																								 decryptedCustName, 
																								 cDetail.getCustomerPhoto(),
																								 (short)1,
																								 Long.parseLong(JSON.get(joNotification, "notificationId")));*/
					JSONObject joNotificationMobile = fcmService.sendAcceptNotificationFriendRequest( fDetail.getFcmToken(), 
																									  fDetail.getCustomerId(), 
																									  decryptedCustName,
																									  fDetail.getCustomerPhoto());
					
					LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
				
				}
				
				if( fDetail.getApnToken() != null && !fDetail.getApnToken().equals("") ) {
					/*Send notification*/
					/*JSONObject joNotificationMobile = apnService.sendNotificationFriendRequest(fDetail.getApnToken(), 
																								 fDetail.getCustomerId(), 
																								 decryptedCustName, 
																								 cDetail.getCustomerPhoto(),
																								 (short)1,
																								 Long.parseLong(JSON.get(joNotification, "notificationId")));*/
					
					JSONObject joNotificationMobile = apnService.sendAcceptNotificationFriendRequest(fDetail.getApnToken(), 
																									 fDetail.getCustomerId(), 
																									 decryptedCustName, 
																									 fDetail.getCustomerPhoto());
					LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotificationMobile.toString());
				}

				LOGGER.debug("[SendNotificationFriendRequest] Result: " + joNotification.toString());
			}
			
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Confirm failed.");
			
		}
		
		
		return jo;
		
	}
	
	
	@Transactional
	@Override
	public JSONObject authenticationHeaderRequest( String token, long userId ) throws ParseException{
		
		/* 1	=>token and userId valid
		 * -1	=>token not valid
		 * -2	=>token expired*/
		
		JSONObject jo 		= new JSONObject();
		List l1 			= customerRepo.tokenAvailableByCustomerId(token, userId);
		List l2 			= customerRepo.isTokenExpired( util.getDateTime(), userId );
		
		if( Integer.parseInt(l1.get(0).toString()) > 0 ){
			
			if( Integer.parseInt(l2.get(0).toString()) == 0 ){
				
				/*Update token expired*/
				
				this.setSessionToken(token, userId);
				
				jo.put("tokenStatus", 1);
				jo.put("tokenMsg", "OK");
				
			}else{
				
				jo.put("tokenStatus", -2);
				jo.put("tokenMsg", "Your session is expired. Please login first.");
				
			}
			
		}else{
			
			jo.put("tokenStatus", -1);
			jo.put("tokenMsg", "Token not valid");
			
		}	
		
		return jo;
		
	}
	
	
	@Transactional
	@Override
	public int setSessionToken( String sessionToken, long customerId ) throws ParseException{
		
		Date tokenExpired = util.addingMinutes(timeout);
		
		return customerRepo.setSessionToken(sessionToken, tokenExpired, customerId);
		
	}
	
	public List validateQRCodeViaAccountNumber( String category, String accountNumber ){
		
		return customerRepo.validateQRCodeViaAccountNumber(category, accountNumber);
		
	}
	
	private void sendNotification( String body,
								   String subject,
								   String destination,
								   String type){	
					
					
		SMSEmailQueue dataSMSEmail	= new SMSEmailQueue();
		dataSMSEmail.setSubject(subject);
		dataSMSEmail.setBody(body);
		dataSMSEmail.setDestinationAccount(destination);
		dataSMSEmail.setType(type);
		dataSMSEmail.setCreateDate(util.localTransactionTime());
		smsEmail.save(dataSMSEmail);
	
	}
	
	@Override
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber,
							    String token){
		
		return customerRepo.validateQRCode( customerId,
										    email,
										    phoneNumber,
										    token);
		
	}
	
	@Override
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber){
					
		return customerRepo.validateQRCode( customerId,
							    			email,
						    				phoneNumber);
	
	}	
	
	public JSONObject getFriendRequested( long customerId ) throws Exception{
	
		JSONObject joResult			= new JSONObject();
		JSONObject joData 	 		= null;
		JSONArray jaData 			= new JSONArray();
		
		List<Object[]> lFriendReq 	= customerRepo.getFriendRequested(customerId);
		Iterator<Object[]> it 		= lFriendReq.iterator();
		String photoURL 			= "";
		
		while( it.hasNext() ){
			
			joData 					= new JSONObject();
			Object[] result 		= (Object[])it.next();
			
			Date inviteAt 			= (Date)result[3];
			Format formatter 		= new SimpleDateFormat("yyyyMMddHHmmss");
			String inviteAtS 		= formatter.format(inviteAt);
			
			if( !((String)result[2]).equals("null") ){
				photoURL 			= customerPhotoURL + (String)result[2];
			}else{
				photoURL 			= "";
			}
			
			joData.put("friendId", (Long)result[0]);
			joData.put("name", AES.decrypt( (String)result[1], dbKey ));
			joData.put("photo", photoURL);
			joData.put("inviteAt", inviteAtS);
			jaData.add(joData);
			
		}
		
		joResult.put("data", jaData);
		joResult.put("errCode", "00");
		joResult.put("errMsg", "OK");
			
		
		return joResult;
		
		
	}
	
	public JSONObject uploadPhoto( MultipartFile file ){
		
		JSONObject joResult			= new JSONObject();
		String fileExtension 		= file.getContentType().split("/")[1];
		String fileName 			= util.generateFileName();
		String fileContentType 		= file.getContentType();
		String filePath				= customerPhotoPath + fileName + "." + fileExtension;
		String urlFile 				= customerPhotoURL + fileName + "." + fileExtension;		
		
		byte[] buffer 				= new byte[3000];
		File outputFile 			= new File( filePath );
		FileInputStream reader  	= null;
		FileOutputStream writer 	= null;
		int totalBytes 				= 0;
		
		try{
			/*Check ext file firs*/
			if( ArrayUtils.contains(photoAllowedExt, fileContentType) ){
				
				outputFile.createNewFile();
				reader 					= (FileInputStream)file.getInputStream();
				writer 					= new FileOutputStream(outputFile);
				int bytesRead 			= 0;
				while( ( bytesRead = reader.read( buffer ) ) != -1 ){
					
					writer.write(buffer);
					totalBytes += bytesRead;
					
				}
				
				joResult.put("errCode","00");
				joResult.put("errMsg", "OK");
				joResult.put("filePath", urlFile);
				joResult.put("fileName", fileName + "." + fileExtension);
				joResult.put("mimeType", file.getContentType());
			}else{
				
				joResult.put("errCode","-99");
				joResult.put("errMsg", "Upload file failed. Not allowed extension.");
				
			}
			
		}catch( IOException ioEx ){
			LOGGER.debug("[CustomerServiceImpl] IOException : " + ioEx.getMessage());
			joResult.put("errCode","-99");
			joResult.put("errMsg", "Upload file failed. Err: " + ioEx.getMessage());
		}finally{
			
			try{
				reader.close();
				writer.close();
			}catch(IOException ioEx){
				LOGGER.debug("[CustomerServiceImpl] IOException : " + ioEx.getMessage());
				joResult.put("errCode","-99");
				joResult.put("errMsg", "Upload file failed. Err: " + ioEx.getMessage());
			}
			
		}
		
		return joResult;
		
	}
	
	
	@Override
	public JSONObject getMobileContent( String type ){
		
		JSONObject joResult			= new JSONObject();
		
		Setting lMContent 			= settingService.findBySettingType(type);
		
		if( lMContent != null ){
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			joResult.put("content", lMContent.getValue());
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Incorrect Content Type.");
		}
		
		
		return joResult;
		
	}
	
	@Transactional
	@Override
	public JSONObject updateNotificationSetting( long customerId, short type, short status ){
		
		JSONObject joResult		= new JSONObject();
		int updateStatus 		= 0;
		
		// type : 1 => voucher notification
		// type : 2 => friend request
		
		if( type == 1 ){			
			updateStatus 		= customerRepo.updateVoucherNotification(status, customerId);
		}else if( type == 2 ){
			updateStatus 		= customerRepo.updateFriendRequestNotification(status, customerId);
		}
		
		if( updateStatus == 1 ){
			joResult.put("errCode", "00");
			joResult.put("errMsg","Update notification setting successfully");
		}else{
			joResult.put("errCode", "-99");
			joResult.put("errMsg","Update notification setting failed");
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getCustomerEVoucher( long customerId ){
		
		JSONObject joResult			= new JSONObject();
		JSONArray jaData 			= new JSONArray();
		JSONObject joData 			= null;
		
		List<Object[]> lVoucher 	= customerRepo.getCustomerEVoucher(customerId);
		
		Iterator<Object[]> it 		= lVoucher.iterator();
		
		while( it.hasNext() ){
			
			Object[] result 		= (Object[])it.next();
			joData 					= new JSONObject();
			
			joData.put("productName", (String)result[0]);
			joData.put("photo", evoucherPhotoURL + (String)result[1]);
			joData.put("voucherValue", (Double)result[2]);
			joData.put("voucherCode", (String)result[3]);
			joData.put("startDate", ((Date)result[4]).toString());
			joData.put("endDate", ((Date)result[5]).toString());
			joData.put("voucherId", (Long)result[6]);
			
			jaData.add(joData);
			
		}
		
		joResult.put("errCode", "00");
		joResult.put("errMsg", "OK");
		joResult.put("data", jaData);
		
		return joResult;
		
	}
	
	@Override
	@Transactional
	public JSONObject inviteFriendByAccountNumber( String accountNumber, long customerId ) throws Exception{
		
		JSONObject joResult			= new JSONObject();
		long friendMemberId 		= 0;
		String decryptedCustName 	= "";
		
		if( accService.validateAccountNumber(accountNumber) ){
			
			List<Account> lAccount	= accService.getAccountDataByAccountNumber(accountNumber);
			
			for( Account a : lAccount ){
				
				friendMemberId 		= a.getMemberId();
				
				//Validate is invite before
				if( fService.checkFriendshipStatus(customerId, friendMemberId) ){
					
					//Validate is has future block invitation
					if( !fService.isFriendRequestFutureBlock(customerId, friendMemberId) ){
					
						//Validate is invited by other person
						Friendship f 	= new Friendship();
						f.setInviteDateTime( util.getDateTime() );
						f.setInvitatorId( customerId );
						f.setInvitedId( friendMemberId );
						f.setType( "just_friend" );
						f.setStatus((short)0);
						f.setInvitationMethod( (short)5  );
						
						joResult		= fService.inviteFriendViaQRCode(f);
						
						/*Request By Pak Edy : 
						 * Get friendName and friendImageURL*/
						Customer fDetail			= customerRepo.findByCustomerId(friendMemberId);
						Customer cDetail 			= customerRepo.findByCustomerId(customerId);
						
						joResult.put("friendName", AES.decrypt( fDetail.getCustomerName() , dbKey));
						if( fDetail.getCustomerPhoto().compareTo("") != 0 ){
							joResult.put("friendImageURL", customerPhotoURL + fDetail.getCustomerPhoto());
						}else{
							joResult.put("friendImageURL","");
						}
						
						decryptedCustName 			 = aes.decrypt( cDetail.getCustomerName(), dbKey);
						
						if( fDetail.getShowFriendRequestNotification() == 1 ){
							
							/*Add to notification*/
							JSONObject joNotification = notificationService.addToNotificationSendFriendRequest(friendMemberId, 
																											   ( cDetail.getCustomerPhoto().equals("") ? "" : ( customerPhotoURL + cDetail.getCustomerPhoto() ) ), 
																											   decryptedCustName);
							
							/*Send notification on Android*/
							if(fDetail.getFcmToken() != null && fDetail.getFcmToken().compareTo("") != 0 ) {
								JSONObject joNotificationMobile = fcmService.sendNotificationFriendRequest(fDetail.getFcmToken(), 
																									 fDetail.getCustomerId(), 
																									 decryptedCustName, 
																									 cDetail.getCustomerPhoto(),
																									 (short)1,
																									 Long.parseLong(JSON.get(joNotification, "notificationId")));
								
								LOGGER.debug("[SendNotificationFriendRequest] Result <Android>: " + joNotificationMobile.toString());
							}
							
							/*Send notification on iOS*/
							if( fDetail.getApnToken() != null && fDetail.getApnToken().compareTo("") != 0 ) {
								
								/*Add to notification*/
								JSONObject joNotificationMobile = apnService.sendNotificationFriendRequest(fDetail.getApnToken(), 
																									 	   fDetail.getCustomerId(), 
																									 	   decryptedCustName, 
																									 	   cDetail.getCustomerPhoto(),
																									 	   (short)1,
																									 	   Long.parseLong(JSON.get(joNotification, "notificationId")));
								
								LOGGER.debug("[SendNotificationFriendRequest] Result <iOS>: " + joNotificationMobile.toString());
								
							}
	
							
						}
						
					}else{
						joResult.put("errCode", "-99");
						joResult.put("errMsg", "Your account is blocked by this member.");
					}
					
				}else{
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "You've friendship this person before");
					
				}
							
				
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "The Account Number not registered on our system");
			
		}
		
		return joResult;
		
	}
	
	@Override
	@Transactional
	public JSONObject inviteFriendByPhoneNumber( String phoneNumber, long customerId ) throws Exception{
		
		JSONObject joResult			= new JSONObject();
		long friendMemberId 		= 0;
		String decryptedCustName 	= "";
			
		Customer c 					= customerRepo.findByCustomerPhoneNumber( AES.encrypt( phoneNumber, dbKey ) );
		
		//Sebelumnya menggunakan List<Customer>
		
		if( c != null ){
			
			friendMemberId 		= c.getCustomerId();
			
			//Validate is invite before
			if( fService.checkFriendshipStatus(friendMemberId, customerId) ){
				
				//Validate is invited by other person
				Friendship f 	= new Friendship();
				f.setInviteDateTime( util.getDateTime() );
				f.setInvitatorId( customerId );
				f.setInvitedId( friendMemberId );
				f.setType( "just_friend" );
				f.setStatus((short)0);
				f.setInvitationMethod( (short)5  );
				
				joResult		= fService.inviteFriendViaQRCode(f);
				
				/*Request By Pak Edy : 
				 * Get friendName and friendImageURL*/
				Customer fDetail			= customerRepo.findByCustomerId(friendMemberId);
				Customer cDetail 			= customerRepo.findByCustomerId(customerId);
				
				joResult.put("friendName", AES.decrypt( fDetail.getCustomerName() , dbKey));
				if( fDetail.getCustomerPhoto().compareTo("") != 0 ){
					joResult.put("friendImageURL", customerPhotoURL + fDetail.getCustomerPhoto());
				}else{
					joResult.put("friendImageURL","");
				}
				
				decryptedCustName 			= aes.decrypt( cDetail.getCustomerName(), dbKey);
				
				if( fDetail.getShowFriendRequestNotification() == 1 ){
					
					/*Add to notification*/
					JSONObject joNotification = notificationService.addToNotificationSendFriendRequest(friendMemberId, 
																									   ( cDetail.getCustomerPhoto().equals("") ? "" : ( customerPhotoURL + cDetail.getCustomerPhoto() ) ), 
																									   decryptedCustName);
					
					/*Send notification on Android*/
					if( fDetail.getFcmToken() != null &&  fDetail.getFcmToken().compareTo("") != 0 ) {
						JSONObject joNotificationMobile = fcmService.sendNotificationFriendRequest(fDetail.getFcmToken(), 
																							 fDetail.getCustomerId(), 
																							 decryptedCustName, 
																							 cDetail.getCustomerPhoto(),
																							 (short)1,
																							 Long.parseLong(JSON.get(joNotification, "notificationId")));
						
						LOGGER.debug("[SendNotificationFriendRequest] Result <Android>: " + joNotificationMobile.toString());
					}
					
					/*Send notification on iOS*/
					if( fDetail.getApnToken() != null && fDetail.getApnToken().compareTo("") != 0 ) {
						
						JSONObject joNotificationMobile = apnService.sendNotificationFriendRequest(fDetail.getApnToken(), 
																							 fDetail.getCustomerId(), 
																							 decryptedCustName, 
																							 cDetail.getCustomerPhoto(),
																							 (short)1,
																							 Long.parseLong(JSON.get(joNotification, "notificationId")));
						
						LOGGER.debug("[SendNotificationFriendRequest] Result <iOS>: " + joNotificationMobile.toString());
						
					}
					
				}
				
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "You've friendship this person before");
				
			}
						
			
		
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "The Phone Number not registered on our system");
			
		}
		
		return joResult;
		
	}
	
	private void friend3OfUs( long newCustomerId ) throws ParseException{
		
		long customerId1 		= 0;
		long customerId2 		= 0;
		long customerId3 		= 0;
		Account acc 			= null;
		
		String accountNumber1 	= "7777799999";
		String accountNumber2  	= "7779997779";
		String accountNumber3 	= "1077107710";
		
		/*Get customerId by accountNumber*/
		acc 					= accService.getCustomerIdByAccountNumber(accountNumber1);
		customerId1				= acc.getMemberId();
		
		acc 					= accService.getCustomerIdByAccountNumber(accountNumber2);
		customerId2				= acc.getMemberId();
		
		acc 					= accService.getCustomerIdByAccountNumber(accountNumber3);
		customerId3				= acc.getMemberId();
		
		Friendship f 		= new Friendship();
		f.setInviteDateTime( util.getDateTime() );
		f.setInvitatorId( customerId1 );
		f.setInvitedId( newCustomerId );
		f.setType( "special_just_friend" );
		f.setStatus((short)1);
		f.setInvitationMethod( (short)4  );
		fService.save(f);
		
		f 					= new Friendship();
		f.setInviteDateTime( util.getDateTime() );
		f.setInvitatorId( customerId2 );
		f.setInvitedId( newCustomerId );
		f.setType( "special_just_friend" );
		f.setStatus((short)1);
		f.setInvitationMethod( (short)4  );
		fService.save(f);
		
		f 					= new Friendship();
		f.setInviteDateTime( util.getDateTime() );
		f.setInvitatorId( customerId3 );
		f.setInvitedId( newCustomerId );
		f.setType( "special_just_friend" );
		f.setStatus((short)1);
		f.setInvitationMethod( (short)4  );
		fService.save(f);
		
	}
	
	@Override
	public JSONObject getFriendDetailByAYARESId( String accountNumber ) throws Exception{
		
		JSONObject joResult		= new JSONObject();
		
		List<Object[]> lDetail	= customerRepo.getFriendDetailByAccountNumber(accountNumber);		
		
		if( lDetail.size() > 0 ){
			
			Iterator<Object[]> it		= lDetail.iterator();
			
			while( it.hasNext() ){
				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "OK");
				
				Object[] result 		= (Object[])it.next();
				joResult.put("name",AES.decrypt( (String)result[0], dbKey ));
				
				
				if( ((String)result[1]).compareTo("") != 0 ){
					joResult.put("photo", customerPhotoURL + (String)result[1]);
				}else{
					joResult.put("photo", "");
				}
				
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
			
		}		
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getFriendDetailByPhoneNumber( String phoneNumber ) throws Exception{
		
		JSONObject joResult		= new JSONObject();
		
		List<Object[]> lDetail	= customerRepo.getFriendDetailByPhoneNumber( AES.encrypt( phoneNumber, dbKey ) );	
		
		System.out.println("### Phone Number : " + phoneNumber);
		
		if( lDetail.size() > 0 ){
			
			Iterator<Object[]> it		= lDetail.iterator();
			
			while( it.hasNext() ){
				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "OK");
				
				Object[] result 		= (Object[])it.next();
				joResult.put("name",AES.decrypt( (String)result[0], dbKey ));
				
				
				if( ((String)result[1]).compareTo("") != 0 ){
					joResult.put("photo", customerPhotoURL + (String)result[1]);
				}else{
					joResult.put("photo", "");
				}
				
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
			
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getFriendProfileById( long friendId, long memberId ) throws Exception{
		
		JSONObject joResult		= new JSONObject();
		
		/*1. Check if friendId is avalaible for this memberId*/
		if( !fService.checkFriendshipStatus(friendId, memberId) ){
		
			List<Object[]> lDetail	= customerRepo.getFriendDetailById( friendId );	
			
			if( lDetail.size() > 0 ){
				
				Iterator<Object[]> it		= lDetail.iterator();
				
				while( it.hasNext() ){
					
					joResult.put("errCode", "00");
					joResult.put("errMsg", "OK");
					
					Object[] result 		= (Object[])it.next();
					joResult.put("name",AES.decrypt( (String)result[0], dbKey ));
					joResult.put("ayaresId", (String)result[4]);
					joResult.put("email", AES.decrypt( (String)result[3], dbKey ));
					joResult.put("phoneNumber", AES.decrypt( (String)result[2], dbKey) );
					
					if( ((String)result[1]).compareTo("") != 0 ){
						joResult.put("photo", customerPhotoURL + (String)result[1]);
					}else{
						joResult.put("photo", "");
					}
					
				}
				
			}else{
				
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "Data not found");
				
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Can't get friend detail data.");
			
		}
		
		return joResult;
		
	}
	
	public Customer findByCustomerPhoneNumber( String phoneNumber ) {
		
		Customer c 					= customerRepo.findByCustomerPhoneNumber( phoneNumber );
		
		return c;
		
	}
	
	@Transactional
	@Override
	public JSONObject refreshProfile( long customerId ) throws Exception{
		
		JSONObject joAuth 		 = new JSONObject();
		JSONObject joData 		 = new JSONObject();
		String sessionToken      = "";
		Date tokenExpired 		 = null;
		String qrCode 			 = "";
		String inviteContent	 = "";
		
		List<Object[]> lCustomer 	= customerRepo.getCustomerDetail( customerId );
		
		
		if( lCustomer.size() > 0 ){
			
			Iterator<Object[]> it 	= lCustomer.iterator();
			
			while( it.hasNext() ){
				
				Object[] result 	= (Object[])it.next();
				
				qrCode 				= aes.encrypt(( (String)result[3] + "|" + 
												    (Long)result[0] + "|" + 
												    (String)result[4] + "|" + 
												    (String)result[12] ), 
												  aesKey);
				
				// Get total Friend Request and Friend List
				List lTotalFR		= customerRepo.getTotalPendingRequest((Long)result[0]);
				List lTotalFL		= customerRepo.getTotalFriendList((Long)result[0]);
				Setting setting		= settingService.findBySettingPurposeAndSettingType("invite_content", 
																					    "sms");
				inviteContent 	  	= formInviteContent( (Long)result[0], (Long)result[6], (String)result[9], setting );
				
				joData.put("customerId", (Long)result[0]);
				joData.put("customerName", AES.decrypt( (String)result[1],dbKey ) );
				joData.put("userLogin", AES.decrypt( (String)result[2], dbKey ) );
				joData.put("email", AES.decrypt( (String)result[3], dbKey) );
				joData.put("customerPhoneNumber", AES.decrypt( (String)result[4], dbKey ) );
				joData.put("accountId", (Long)result[6]);
				joData.put("qrCode", qrCode);
				joData.put("balance", (Double)result[7]);
				joData.put("photo", customerPhotoURL + (String)result[8]);
				joData.put("totalFriendRequest", Integer.parseInt( lTotalFR.get(0).toString()));
				joData.put("totalFriendList", Integer.parseInt( lTotalFL.get(0).toString()));
				joData.put("accountNumber", (String)result[9]);	
				joData.put("inviteContent", inviteContent);
				joData.put("showVoucherNotification", (short)result[10]);
				joData.put("showFriendRequestNotification", (short)result[11]);
				
				joAuth.put("errCode", "00");
				joAuth.put("errMsg", "OK");
				joAuth.put("data",joData);
				
			}			
			
		}else{
			
			joAuth.put("errCode", "-99");
			joAuth.put("errMsg", "Login failed");
			
		}
		
		return joAuth;
	}
	
	@Override
	@Transactional
	public JSONObject updateFCMToken( long customerId, String fcmToken ){
		
		JSONObject joResult 		= new JSONObject();
		
		List lCheck 				= customerRepo.validateByCustomerId(customerId);
		
		if( Integer.parseInt( lCheck.get(0).toString() ) > 0 ){
			
			int updateResult 		= customerRepo.updateFCMToken(fcmToken, customerId);
			
			if( updateResult > 0 ){				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "FCM Token successfully updated");				
			}else{
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "FCM Token failed updated");
			}
			
		}else{			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Customer not found");				
		}
		
		return joResult;
		
	}
	
	@Override
	@Transactional
	public JSONObject updateAPNSToken( long customerId, String apnToken ){
		
		JSONObject joResult 		= new JSONObject();
		
		List lCheck 				= customerRepo.validateByCustomerId(customerId);
		
		if( Integer.parseInt( lCheck.get(0).toString() ) > 0 ){
			
			int updateResult 		= customerRepo.updateAPNSToken(apnToken, customerId);
			
			if( updateResult > 0 ){				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "APNS Token successfully updated");				
			}else{
				joResult.put("errCode", "-99");
				joResult.put("errMsg", "APNS Token failed updated");
			}
			
		}else{			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Customer not found");				
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getWithdrawlHistories( long customerId,
											 int start,
											 int limit){
		
		JSONObject joResult 		= new JSONObject();		
		long accountId 				= 0;
		List lAccount 				= customerRepo.getAccountIdByCustomerId(customerId);
		
		if( lAccount.isEmpty() ){
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
			
		}else{
			
			accountId 				= Long.parseLong( lAccount.get(0).toString() );
			
			joResult 				= withdrawlService.getWithdrawlHistory(accountId, start, limit);
			
		}
		
		return joResult;
		
	}
	
	
	private JSONObject inviteFriendUsingReferalId( String accountNumber, long customerId ) throws Exception{
		
		JSONObject joResult			= new JSONObject();
		long friendMemberId 		= 0;
		
		if( accService.validateAccountNumber(accountNumber) ){
			
			List<Account> lAccount	= accService.getAccountDataByAccountNumber(accountNumber);
			
			for( Account a : lAccount ){
				
				friendMemberId 		= a.getMemberId();
				
				//Validate is invite before
				if( fService.checkFriendshipStatus(friendMemberId, customerId) ){
					
					Friendship f 	= new Friendship();
					f.setInviteDateTime( util.getDateTime() );
					f.setInvitatorId( friendMemberId );
					f.setInvitedId( customerId );
					f.setType( "invited" );
					f.setStatus((short)1);
					f.setInvitationMethod( (short)2  );
					
					fService.inviteFriendViaQRCode(f);
					
					/*Request By Pak Edy : 
					 * Get friendName and friendImageURL*/
					Customer fDetail			= customerRepo.findByCustomerId(friendMemberId);
					joResult.put("friendName", AES.decrypt( fDetail.getCustomerName() , dbKey));
					if( fDetail.getCustomerPhoto().compareTo("") != 0 ){
						joResult.put("friendImageURL", customerPhotoURL + fDetail.getCustomerPhoto());
					}else{
						joResult.put("friendImageURL","");
					}
					
				}else{
					
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "You've friendship this person before");
					
				}
							
				
			}
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "The Account Number not registered on our system");
			
		}
		
		return joResult;
		
	}
	
	
	@Override
	public Customer getCustomerDetailByCustomerId( long customerId ){
		return customerRepo.findByCustomerId(customerId);
	}
	
	private String formInviteContent( long customerId, long accountId, String accountNumber, Setting setting ) throws Exception{
		
		String ayaresId 		= "";
		String simpleUrl 		= "";
		String shareLinkMessage = "";
		String shareLinkUrlNew  = "";
		String urlTinyURLNew    = "";
		
		// Form ayares_id for referal
		//AES(customerId|accountNumber, aesKey)
		ayaresId 			= Long.toString(customerId) + "|" + accountId + "|" + accountNumber;
		ayaresId			= aes.encrypt(ayaresId, aesKey);				
		
		
		shareLinkUrlNew 	= shareLinkUrl.replace("[#ayares_id#]", ayaresId);
		
		urlTinyURLNew 		= urlTinyURL.replace("[#url#]", shareLinkUrlNew);
		
		simpleUrl 			= util.sendGet(urlTinyURLNew);				
		shareLinkMessage  	= setting.getValue().replace("[#share_link_url#]", simpleUrl);
		
		return shareLinkMessage;
		
	}	
	
	@Override
	public JSONObject getCustomerByName( String keyword, long userId ) {
		
		JSONObject joResult = new JSONObject();
		JSONArray jaData 	= new JSONArray();
		JSONObject joData 	= new JSONObject();
		
		if( !keyword.equals("") ) {
			
			if( keyword.length() >= 4 ) {
				
				List<Object[]> lCustomer 		= manager.createNativeQuery("CALL sp_get_customer_list ( ?, ? )")
													.setParameter(1, "%" + keyword.trim() + "%")
													.setParameter(2, dbKey)
													.getResultList();
				
				
				Iterator<Object[]> it 			= lCustomer.iterator();
				
				while( it.hasNext() ) {
					
					Object[] result 			= (Object[])it.next();
					joData 						= new JSONObject();
					
					if( ((String)result[0]).compareTo("") != 0 ){
						joData.put("photo", customerPhotoURL + (String)result[0]);
					}else{
						joData.put("photo", "");
					}
					
					joData.put("name", (String)result[1]);
					joData.put("phoneNumber", (String)result[2]);
					joData.put("ayaresId", (String)result[3]);
					
					jaData.add(joData);
					
				}
				
				joResult.put("errCode", "00");
				joResult.put("errMsg", "OK");
				joResult.put("data", jaData);
				
			}else {
				joResult.put("errCode","-99");
				joResult.put("errMsg", "The keyword is not valid. Please re-enter min. 3 characters.");
			}
			
		}else {
			joResult.put("errCode","-99");
			joResult.put("errMsg", "The keyword can't be empty.");
		}
		
		return joResult;
		
	}
	
	@Transactional
	@Override
	public JSONObject loginViaMedsos( String oAuthToken,
									  String app,
									  String medsos,
									  String oAuthSecret ) throws Exception {
		
		JSONObject joResult 			= new JSONObject();
		JSONObject joResultLoginMedsos	= new JSONObject();
		JSONObject joResultRegister		= new JSONObject();
		JSONObject joResultLoginAYARES 	= new JSONObject();
		
		if( medsos.equals("fb") ) {
			
			joResultLoginMedsos 	= oAuthService.loginViaFB( oAuthToken );
		
		}else if( medsos.equals("gmail") ) {
			
			joResultLoginMedsos 	= oAuthService.loginViaGoogle( oAuthToken );
		
		}else if( medsos.equals("twitter") ) {
			
			joResultLoginMedsos 	= oAuthService.loginViaTwitter(oAuthToken, oAuthSecret);
		
		}
		
		if( JSON.get(joResultLoginMedsos, "errCode").equals("00") ) {
			
			joResultRegister 		= this.register(JSON.get(joResultLoginMedsos, "customerName"), 
													"", 
													"", 
													JSON.get(joResultLoginMedsos, "email"), 
													"", 
													JSON.get(joResultLoginMedsos, "photo"), 
													app, 
													(short)1, 
													(short)0, 
													"", 
													(short)1, 
													medsos,
													JSON.get(joResultLoginMedsos, "id"));

			if( JSON.get(joResultRegister, "errCode").equals("00") ) {

					joResultLoginAYARES	= this.authLogin(JSON.get(joResultLoginMedsos, "email"), 
														 "", 
														 (short)1, 
														 medsos, 
														 app);

					joResult 			= joResultLoginAYARES;

			}else {

					joResult = joResultRegister;

			}
			
		}else {
			
			joResult 		 = joResultLoginMedsos;
			
		}
		
		return joResult;
		
	}
	
	
	
}

