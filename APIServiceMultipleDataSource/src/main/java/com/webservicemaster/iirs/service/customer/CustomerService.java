package com.webservicemaster.iirs.service.customer;

import java.text.ParseException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.webservicemaster.iirs.domain.master.Customer;

public interface CustomerService {

	public JSONObject authLogin( String uLogin, 
								 String uPassword,
								 short isLoginViaMedsos,
								 String medsos,
								 String app) throws Exception;
	
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
							    String medsosId) throws Exception;
	
	public JSONObject authenticationHeaderRequest( String token, 
												   long userId ) throws ParseException;
	
	public int setSessionToken( String sessionToken, long customerId ) throws ParseException;
	
	public JSONObject forgotPassword( String email ) throws Exception;
	
	public JSONObject updatePassword( long customerId,
									  String currentPassword,
									  String newPassword );
	
	public JSONObject updateProfile( long customerId,
									 String customerName,
									 String userLogin,
									 String email,
									 String customerPhoneNumber,
									 String customerPhoto) throws Exception;
	
	public JSONObject inviteFriend( long customerId,
									String friendId );
	
	public JSONObject confirmFriendship( long customerId, 
									     short status, 
									     short isBlockFutureInvitation,
									     long friendId ) throws ParseException, Exception;
	
	public JSONObject inviteJustFriend( JSONArray arrPhoneNumber,
										long customerId ) throws Exception;
	
	public List validateQRCodeViaAccountNumber( String category, String accountNumber );
	
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber,
							    String token);
	
	public List validateQRCode( long customerId,
							    String email,
							    String phoneNumber);
	
	public JSONObject getFriendRequested( long customerId ) throws Exception;
	
	public JSONObject uploadPhoto( MultipartFile file );
	
	public JSONObject getMobileContent( String type );
	
	public JSONObject updateNotificationSetting( long customerId, short type, short status );
	
	public JSONObject getCustomerEVoucher( long customerId );
	
	public JSONObject inviteFriendByAccountNumber( String accountNumber, long customerId ) throws Exception;
	
	public JSONObject getFriendDetailByAYARESId( String accountNumber )throws Exception;
	
	public JSONObject getFriendDetailByPhoneNumber( String phoneNumber ) throws Exception;
	
	public JSONObject inviteFriendByPhoneNumber( String phoneNumber, long customerId ) throws Exception;
	
	public JSONObject getFriendProfileById( long friendId, long memberId ) throws Exception;
	
	public Customer findByCustomerPhoneNumber( String phoneNumber );
	
	public JSONObject refreshProfile( long customerId ) throws Exception;
	
	public JSONObject updateFCMToken( long customerId, String fcmToken );
	
	public JSONObject updateAPNSToken( long customerId, String apnToken );
	
	public JSONObject getWithdrawlHistories( long customerId, int start, int limit);
	
	public Customer getCustomerDetailByCustomerId( long customerId );
	
	public JSONObject getMyProfile( long customerId ) throws Exception;
	
	public JSONObject getCustomerByName( String keyword, long userId );
	
	public JSONObject loginViaMedsos( String oAuthToken,
									  String app,
									  String medsos,
									  String oAuthSecret) throws Exception;
	
}
