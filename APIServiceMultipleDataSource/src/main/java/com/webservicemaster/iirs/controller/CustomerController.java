package com.webservicemaster.iirs.controller;

import java.text.ParseException;

//import javax.servlet.annotation.MultipartConfig;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.domain.master.CustomerAccountBank;
import com.webservicemaster.iirs.service.advertise.AdvertiseService;
import com.webservicemaster.iirs.service.customer.CustomerService;
import com.webservicemaster.iirs.service.customeraccountbank.CustomerAccountBankService;
import com.webservicemaster.iirs.service.friendship.FriendshipService;
import com.webservicemaster.iirs.service.notification.NotificationService;
import com.webservicemaster.iirs.service.transaction.TransactionService;
import com.webservicemaster.iirs.service.transactiondetail.TransactionDetailService;
import com.webservicemaster.iirs.service.withdrawl.WithdrawlService;
import com.webservicemaster.iirs.utility.JSON;

@RestController
@RequestMapping("/api.mobile/member")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdvertiseService advService;
	
	@Autowired
	private FriendshipService friendService;
	
	@Autowired
	private TransactionDetailService transDetailService;
	
	@Autowired
	private WithdrawlService witdrawlService;
	
	@Autowired
	private CustomerAccountBankService customerAccountBankService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private TransactionService transService;
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String doLogin( @RequestBody String jsonParam ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joResultLogin 	= new JSONObject();		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		
		logger.debug("[doLogin] ### START ###");
		logger.debug("[doLogin] HEADER : -");
		logger.debug("[doLogin] BODY : " + jsonParam);
		logger.debug("[doLogin] ### END ###");
		
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		joResultLogin 				= customerService.authLogin( JSON.get(joParam, "userLogin"), 
																 JSON.get(joParam, "userPassword"),
																 (short)0,
																 "apps",
																 JSON.get(joParam, "app"));		
		return joResultLogin.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/loginViaMedsos", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String doLoginViaMedsos( @RequestBody String jsonParam ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joResultLogin 	= new JSONObject();		
		JSONObject joResultRegister = new JSONObject();
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		/*joResultRegister 			= customerService.register( JSON.get(joParam, "customerName"), 
																JSON.get(joParam, "userLogin"), 
																JSON.get(joParam, "userPassword"),
																JSON.get(joParam, "email"), 
																JSON.get(joParam, "customerPhoneNumber"), 
																JSON.get(joParam, "photo"),
																JSON.get(joParam, "app"),
																(short)1,
																(short)0,
																"",
																(short)1,
																JSON.get(joParam, "medsos"));	
		
		joResultLogin 				= customerService.authLogin(JSON.get(joParam, "email"), 
																"", 
																(short)1, 
																JSON.get(joParam, "medsos"), 
																JSON.get(joParam, "app"));*/
		
		joResultLogin 				= customerService.loginViaMedsos(JSON.get(joParam, "oAuthToken"), 
																	 JSON.get(joParam, "app"), 
																	 JSON.get(joParam, "medsos"), 
																	 JSON.get(joParam, "oAuthTokenSecret"));
		
		return joResultLogin.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/register", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String register( @RequestBody String jsonParam ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		logger.debug("[register] ### START ###");
		logger.debug("[register] HEADER : -");
		logger.debug("[register] BODY : " + jsonParam);
		logger.debug("[register] ### END ###");
		
		joResult 					= customerService.register( JSON.get(joParam, "customerName"), 
																JSON.get(joParam, "userLogin"), 
																JSON.get(joParam, "userPassword"),
																JSON.get(joParam, "email"), 
																JSON.get(joParam, "customerPhoneNumber"), 
																JSON.get(joParam, "photo"),
																JSON.get(joParam, "app"),
																Short.parseShort( JSON.get(joParam, "acceptTermCondition") ),
																Short.parseShort( JSON.get(joParam, "referalMode") ),
																JSON.get(joParam, "referalNumber"),
																(short)0,
																"",
																"");
		
		// Additional transaction after registration
		if( JSON.get(joResult, "errCode").equals("00") ) {
			transService.getIncentiveAfterRegister(Integer.parseInt(JSON.get(joResult, "customerId")));
		}	
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String uploadPhoto( @RequestParam("file") MultipartFile file ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();		
		
		joResult 					= customerService.uploadPhoto(file);
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/forgotPassword", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String forgotPassword( @RequestBody String jsonParam ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		logger.debug("[forgotPassword] ### START ###");
		logger.debug("[forgotPassword] HEADER : -");
		logger.debug("[forgotPassword] BODY : " + jsonParam);
		logger.debug("[forgotPassword] ### END ###");
		
		joResult 					= customerService.forgotPassword( JSON.get(joParam, "email") );
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updatePassword", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updatePassword( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									@RequestHeader(value="X-ID") int userId,
  		  										@RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		
		logger.debug("[updatePassword] ### START ###");
		logger.debug("[updatePassword] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updatePassword] BODY : " + jsonParam);
		logger.debug("[updatePassword] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.updatePassword( JSON.getInt(joParam, "customerId") , 
																	  JSON.get(joParam, "currentPassword"), 
																	  JSON.get(joParam, "newPassword"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updateProfile", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updateProfile( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									@RequestHeader(value="X-ID") int userId,
  		  										@RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		Logger logger 				= LoggerFactory.getLogger(Customer.class);
		
		logger.debug("[updateProfile] ### START ###");
		logger.debug("[updateProfile] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updateProfile] BODY : " + jsonParam);
		logger.debug("[updateProfile] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.updateProfile(Long.parseLong( JSON.get(joParam, "customerId") ), 
																	JSON.get(joParam, "customerName"), 
																	JSON.get(joParam, "userLogin"), 
																	JSON.get(joParam, "email"), 
																	JSON.get(joParam, "phoneNumber"),
																	JSON.get(joParam, "customerPhoto"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/inviteFriendViaQRCode", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String inviteFriendViaQRCode( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										   @RequestHeader(value="X-ID") long userId,
	  		  										   @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		String qrCode 			 	= JSON.get(joParam, "qrcode");
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[inviteFriendViaQRCode] ### START ###");
		logger.debug("[inviteFriendViaQRCode] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[inviteFriendViaQRCode] BODY : " + jsonParam);
		logger.debug("[inviteFriendViaQRCode] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){	
			
			if( qrCode.toLowerCase().contains("ayares") ) {
				qrCode = qrCode.replace("ayares", "");
			}
			
			joResult				= customerService.inviteFriend( userId , 
																    qrCode );
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/inviteJustFriend", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String inviteJustFriend( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									  @RequestHeader(value="X-ID") int userId,
	  		  									  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );

		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[inviteJustFriend] ### START ###");
		logger.debug("[inviteJustFriend] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[inviteJustFriend] BODY : " + jsonParam);
		logger.debug("[inviteJustFriend] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.inviteJustFriend( JSON.newJSONArray( JSON.get(joParam, "friendPhoneNumber") ),
																		Long.parseLong( JSON.get(joParam, "customerId") ) );
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/confirmFriendship", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String confirmFriendship( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
												   @RequestHeader(value="X-ID") long userId,
												   @RequestBody String jsonParam ) throws Exception{
		
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[confirmFriendship] ### START ###");
		logger.debug("[confirmFriendship] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[confirmFriendship] BODY : " + jsonParam);
		logger.debug("[confirmFriendship] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult 					= customerService.confirmFriendship(userId,
																		    Short.parseShort( JSON.get(joParam, "status") ), 
																		    Short.parseShort( JSON.get(joParam, "isBlockFutureInvitation") ), 
																			Long.parseLong( JSON.get(joParam, "friendId") ));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getFriendRequest", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getFriendRequest( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									  @RequestHeader(value="X-ID") long customerId ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getFriendRequest] ### START ###");
		logger.debug("[getFriendRequest] HEADER : X-ID = " + customerId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getFriendRequest] BODY : - ");
		logger.debug("[getFriendRequest] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getFriendRequested( customerId );
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMobileContent", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMobileContent( @RequestBody String jsonParam) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getMobileContent] ### START ###");
		logger.debug("[getMobileContent] HEADER : -");
		logger.debug("[getMobileContent] BODY : " + jsonParam);
		logger.debug("[getMobileContent] ### END ###");
		
		joResult					= customerService.getMobileContent( JSON.get(joParam, "contentType") );
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getAdvertisement", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getAdvertisement( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
				  								  @RequestHeader(value="X-ID") long customerId ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getAdvertisement] ### START ###");
		logger.debug("[getAdvertisement] HEADER : X-ID = " + customerId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getAdvertisement] BODY : -");
		logger.debug("[getAdvertisement] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= advService.getAdvertise();
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getFriendList", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getFriendList( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  								   @RequestHeader(value="X-ID") long customerId,
	  		  								   @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam	 		= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getFriendList] ### START ###");
		logger.debug("[getFriendList] HEADER : X-ID = " + customerId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getFriendList] BODY : " + jsonParam);
		logger.debug("[getFriendList] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= friendService.getFriendList( customerId,
																   JSON.getInt(joParam, "start"),
																   JSON.getInt(joParam, "limit"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updateNotificationSetting", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updateNotificationSetting( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  											   @RequestHeader(value="X-ID") long userId,
	  		  											   @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[updateNotificationSetting] ### START ###");
		logger.debug("[updateNotificationSetting] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updateNotificationSetting] BODY : " + jsonParam);
		logger.debug("[updateNotificationSetting] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.updateNotificationSetting(userId, 
																				Short.parseShort( JSON.get(joParam, "type") ), 
																				Short.parseShort( JSON.get(joParam, "status") ));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getEVoucher", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getEVoucher( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  								 @RequestHeader(value="X-ID") long customerId ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getEVoucher] ### START ###");
		logger.debug("[getEVoucher] HEADER : X-ID = " + customerId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getEVoucher] BODY : -");
		logger.debug("[getEVoucher] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getCustomerEVoucher(customerId);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}	
	
	@RequestMapping( value="/getTransactionHistories", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String getTransactionHistories( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
														 @RequestHeader(value="X-ID") int userId,
														 @RequestBody String param ) throws ParseException{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getTransactionHistories] ### START ###");
		logger.debug("[getTransactionHistories] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getTransactionHistories] BODY : " + param);
		logger.debug("[getTransactionHistories] ### END ###");
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
		
			joResult 					= transDetailService.getTransactionHistory( Long.parseLong( JSON.get(joParam, "accountId") ),
																	          	    JSON.get(joParam, "keyword"),
																	          	    JSON.getInt(joParam, "start"),
																	          	    JSON.getInt(joParam, "limit"));	
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
			
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/inviteFriendViaAccountNumber", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String inviteFriendViaAccountNumber( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										   		  @RequestHeader(value="X-ID") long userId,
	  		  										   		  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[inviteFriendViaAccountNumber] ### START ###");
		logger.debug("[inviteFriendViaAccountNumber] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[inviteFriendViaAccountNumber] BODY : " + jsonParam);
		logger.debug("[inviteFriendViaAccountNumber] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.inviteFriendByAccountNumber(JSON.get(joParam, "accountNumber"), 
																				  Long.parseLong( JSON.get(joParam, "customerId") ));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/inviteFriendViaPhoneNumber", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String inviteFriendViaPhoneNumber( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										   		@RequestHeader(value="X-ID") long userId,
	  		  										   		@RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[inviteFriendViaPhoneNumber] ### START ###");
		logger.debug("[inviteFriendViaPhoneNumber] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[inviteFriendViaPhoneNumber] BODY : " + jsonParam);
		logger.debug("[inviteFriendViaPhoneNumber] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.inviteFriendByPhoneNumber(JSON.get(joParam, "phoneNumber"), 
																				Long.parseLong( JSON.get(joParam, "customerId") ));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMemberDetailById", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMemberDetailById( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										   	  @RequestHeader(value="X-ID") long userId,
	  		  										   	  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getMemberDetailById] ### START ###");
		logger.debug("[getMemberDetailById] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getMemberDetailById] BODY : " + jsonParam);
		logger.debug("[getMemberDetailById] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getFriendDetailByAYARESId(JSON.get(joParam, "ayaresId"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMemberDetailByPhoneNumber", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMemberDetailByPhoneNumber( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										 		  @RequestHeader(value="X-ID") long userId,
	  		  										 		  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getMemberDetailByPhoneNumber] ### START ###");
		logger.debug("[getMemberDetailByPhoneNumber] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getMemberDetailByPhoneNumber] BODY : " + jsonParam);
		logger.debug("[getMemberDetailByPhoneNumber] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getFriendDetailByPhoneNumber(JSON.get(joParam, "phoneNumber"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMemberDetailByName", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMemberDetailByName( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										 		  @RequestHeader(value="X-ID") long userId,
	  		  										 		  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getMemberDetailByPhoneNumber] ### START ###");
		logger.debug("[getMemberDetailByPhoneNumber] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getMemberDetailByPhoneNumber] BODY : " + jsonParam);
		logger.debug("[getMemberDetailByPhoneNumber] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getCustomerByName(JSON.get(joParam, "keyword"), userId);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getFriendProfileById", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getFriendProfileById( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										 		  @RequestHeader(value="X-ID") long userId,
	  		  										 		  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getFriendProfileById] ### START ###");
		logger.debug("[getFriendProfileById] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getFriendProfileById] BODY : " + jsonParam);
		logger.debug("[getFriendProfileById] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getFriendProfileById(Long.parseLong( JSON.get(joParam, "friendId") ),
																		   userId);
				
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/checkPhoneNumberIsMember", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String checkPhoneNumberIsMember( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  										      @RequestHeader(value="X-ID") long userId,
	  		  										 	  @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[checkPhoneNumberIsMember] ### START ###");
		logger.debug("[checkPhoneNumberIsMember] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[checkPhoneNumberIsMember] BODY : " + jsonParam);
		logger.debug("[checkPhoneNumberIsMember] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= friendService.checkPhoneNumberIsMember(userId, 
																			 JSON.newJSONArray( JSON.get(joParam, "contactPhoneNumber").toString() ));
				
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/refreshProfile", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String refreshProfile( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									@RequestHeader(value="X-ID") int userId) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[refreshProfile] ### START ###");
		logger.debug("[refreshProfile] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[refreshProfile] BODY : -");
		logger.debug("[refreshProfile] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.refreshProfile(userId);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/withdrawl", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String withdrawl( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  							   @RequestHeader(value="X-ID") long userId,
	  		  							   @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[withdrawl] ### START ###");
		logger.debug("[withdrawl] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[withdrawl] BODY : " + jsonParam);
		logger.debug("[withdrawl] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= witdrawlService.transactionWithdrawl(userId, 
																		   Double.parseDouble( JSON.get(joParam, "amount") ) );		
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updateAccountBank", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updateAccountBank( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  							   		   @RequestHeader(value="X-ID") long userId,
	  		  							   		   @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[updateAccountBank] ### START ###");
		logger.debug("[updateAccountBank] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updateAccountBank] BODY : " + jsonParam);
		logger.debug("[updateAccountBank] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerAccountBankService.updateAccountBankSetting(userId, 
																						  JSON.get(joParam, "bankCode"), 
																						  JSON.get(joParam, "accountNumber"),  
																						  JSON.get(joParam, "accountName"),  
																						  JSON.get(joParam, "identityCard"),  
																						  JSON.get(joParam, "file") );
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/deleteAccountBank", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteAccountBank( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  							   		   @RequestHeader(value="X-ID") long userId) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);			
		logger.debug("[deleteAccountBank] ### START ###");
		logger.debug("[deleteAccountBank] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[deleteAccountBank] BODY : -");
		logger.debug("[deleteAccountBank] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerAccountBankService.deleteAccountBankSetting(userId);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/uploadKTP", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String uploadKTP( @RequestParam("file") MultipartFile file,
										   @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
										   @RequestHeader(value="X-ID") long userId) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[uploadKTP] ### START ###");
		logger.debug("[uploadKTP] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[uploadKTP] BODY : -");
		logger.debug("[uploadKTP] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult 					= customerAccountBankService.uploadIdentityCard(file);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updateFCMToken", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updateFCMToken( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									@RequestHeader(value="X-ID") int userId,
  		  										@RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[updateFCMToken] ### START ###");
		logger.debug("[updateFCMToken] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updateFCMToken] BODY : " + jsonParam);
		logger.debug("[updateFCMToken] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.updateFCMToken(userId, JSON.get(joParam, "fcmToken"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/updateAPNSToken", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String updateAPNSToken( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  									@RequestHeader(value="X-ID") int userId,
  		  										@RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[updateFCMToken] ### START [updateAPNSToken] ###");
		logger.debug("[updateFCMToken] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updateFCMToken] BODY : " + jsonParam);
		logger.debug("[updateFCMToken] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.updateAPNSToken(userId, JSON.get(joParam, "fcmToken"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
		
	@RequestMapping( value="/getWithdrawlHistories", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String getWithdrawlHistories( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
													   @RequestHeader(value="X-ID") int userId,
													   @RequestBody String param ) throws ParseException{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getWithdrawlHistories] ### START ###");
		logger.debug("[getWithdrawlHistories] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getWithdrawlHistories] BODY : " + param);
		logger.debug("[getWithdrawlHistories] ### END ###");
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
		
			joResult 					= customerService.getWithdrawlHistories(userId, 
																				JSON.getInt(joParam, "start"), 
																			    JSON.getInt(joParam, "limit"));
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
			
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMyProfile", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody String getMyProfile( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  								  @RequestHeader(value="X-ID") long userId ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		JSONObject joResult 		= new JSONObject();
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getFriendProfileById] ### START ###");
		logger.debug("[getFriendProfileById] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getFriendProfileById] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult				= customerService.getMyProfile(userId);
				
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getNotificationList", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getNotificationList( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  								   		 @RequestHeader(value="X-ID") long customerId,
	  		  								   		 @RequestBody String jsonParam ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		JSONObject joParam	 		= JSON.newJSONObject( jsonParam );
		
		Logger logger 				= LoggerFactory.getLogger(Customer.class);		
		logger.debug("[getFriendList] ### START ###");
		logger.debug("[getFriendList] HEADER : X-ID = " + customerId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[getFriendList] BODY : " + jsonParam);
		logger.debug("[getFriendList] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult 				= notificationService.getNotificationList(customerId,
																			  JSON.get(joParam, "keyword"), 
																			  JSON.getInt(joParam, "start"), 
																			  JSON.getInt(joParam, "limit"));
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/deleteNotification", method=RequestMethod.DELETE, produces="application/json")
	public @ResponseBody String deleteNotification( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
	  		  								   		 @RequestHeader(value="X-ID") long customerId ) throws Exception{
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, customerId);
		JSONObject joResult 		= new JSONObject();
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			joResult 				= notificationService.deleteNotification( customerId );
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));		
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
		
		return joResult.toString();
		
	}	
	
	@RequestMapping( value="/transactionRebate", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String transactionRebate( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
												   @RequestHeader(value="X-ID") int userId,
												   @RequestBody String param ) throws NumberFormatException, Exception{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= customerService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
		
			joResult 					= transService.transactionUsingCustomerApps(userId, 
																					JSON.get(joParam, "merchantCode"), 
																					JSON.get(joParam, "cashierId"), 
																					JSON.get(joParam, "password"), 
																					Double.parseDouble( JSON.get(joParam, "total") ));
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
			
		return joResult.toString();
		
	}
	
}
