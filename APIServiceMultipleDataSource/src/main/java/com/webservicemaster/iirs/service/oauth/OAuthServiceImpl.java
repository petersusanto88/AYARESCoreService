package com.webservicemaster.iirs.service.oauth;

import java.io.IOException;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.intuit.ipp.exception.FMSException;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class OAuthServiceImpl implements OAuthService {

	private static final Logger log = LoggerFactory.getLogger( OAuthServiceImpl.class );
	
	@Value("${oAuth.login.fb}")
	private String urlOAuthFB;
	
	@Value("${oAuth.login.google}")
	private String urlOAuthGoogle;
	
	@Value("${customer.photo.path}")
	private String customerPhotoPath;
	
	@Value("${oAuth.login.google.key}")
	private String oAuthGoogleKey;
	
	@Value("${oAuth.login.twitter}")
	private String urlOAuthTwitter;
	
	@Value("${oAuth.login.twitter.consumerKey}")
	private String oAuthTwitterConsumerKey;
	
	@Value("${oAuth.login.twitter.consumerSecret}")
	private String oAuthTwitterConsumerSecret;
	
	@Autowired
	private Utility util;
	
	@Override
	public JSONObject loginViaFB( String accessToken ) throws Exception {
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joResultLogin 	= null;
		String urlLogin 			= urlOAuthFB + "?access_token=" 
									  + accessToken 
									  + "&fields=first_name,last_name,gender,email,picture,name";
		String loginResult 			= util.sendGet( urlLogin );		
		
		JSONObject joPicture 		= null;
		JSONObject joData 			= null;
		String fileName 			= "";
		
		joResultLogin 				= JSON.newJSONObject(loginResult);
		
		if( !joResultLogin.containsKey("error") ) {
			joResult.put("customerName", JSON.get(joResultLogin, "name"));
			joResult.put("email", JSON.get(joResultLogin, "email"));
			joResult.put("id", JSON.get(joResultLogin, "id"));
			
			if( joResult.containsKey("picture") ) {
				joPicture 					= JSON.newJSONObject( JSON.get(joResultLogin, "picture") );
				if( joPicture.containsKey("data") ) {
					joData 					= JSON.newJSONObject( JSON.get( joPicture , "data") );
					if( joData.containsKey("url") ) {
						
						/*Get Filename*/
						fileName 			= util.getLastBitFromUrl( JSON.get(joData, "url") ); 
						
						/*Get Photo*/
						util.DownloadImage( JSON.get(joData, "url"), 
											customerPhotoPath + fileName);
						
						joResult.put("photo", fileName);
						
					}
				}
			}	
			
			joResult.put("errCode", "00");
			
		}else {
			
			JSONObject joError				= JSON.newJSONObject( JSON.get(joResultLogin, "error") );
			
			joResult.put("errCode", JSON.get(joError, "code"));
			joResult.put("errMsg", JSON.get(joError, "message"));
		
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject loginViaGoogle( String accessToken ) throws IOException {
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joResultLogin 	= null;
		String urlLogin 			= urlOAuthGoogle + "?key=" 
									  + oAuthGoogleKey;
		String loginResult 			= "";
		
		JSONObject joPicture 		= null;
		JSONObject joData 			= null;
		String fileName 			= "";
		
		JSONObject joBody 			= new JSONObject();
		joBody.put("idToken", accessToken);	
		
		loginResult 				= util.sendRequest(urlLogin, joBody.toString(), "application/json");
		joResultLogin 				= JSON.newJSONObject(loginResult);
		
		System.out.println("### URL Login : " + urlLogin);
		System.out.println("### Body : " + joBody);
		System.out.println("### Login Result : " + loginResult);
		
		if( !joResultLogin.containsKey("error") ) {
			
			if( joResultLogin.containsKey("users") ) {
				
				JSONArray jaUser 	= JSON.newJSONArray( JSON.get(joResultLogin, "users") );
				
				if( jaUser.size() > 0 ) {
					
					JSONObject joUser	= JSON.newJSONObject( jaUser.get(0).toString() );
					joResult.put("errCode", "00");
					joResult.put("customerName", JSON.get(joUser, "displayName"));
					joResult.put("email", JSON.get(joUser, "email"));
					joResult.put("id", JSON.get(joUser, "localId"));
					
					if( joUser.containsKey("photoUrl") ) {
						
						/*Get Filename*/
						fileName 			= util.generateFileName() + ".jpg";
						
						/*Get Photo*/
						util.DownloadImage( JSON.get(joUser, "photoUrl"), 
											(customerPhotoPath + fileName));
						
						joResult.put("photo", fileName);
						
					}
					
				}else {
					joResult.put("errCode", "-99");
					joResult.put("errMsg", "No data user");
				}
				
				
			}
			
		}else {
			
			JSONObject joError 		= JSON.newJSONObject( JSON.get(joResultLogin, "error") );
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "(" + JSON.get(joError, "code") + ")" + JSON.get(joError, "message"));
			
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject loginViaTwitter( String accessToken, String accessTokenSecret ) throws FMSException, IOException {
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joLoginResult 	= null;
		String loginResult 			= "";
		String fileName 			= "";
		
		loginResult 				= util.sendOAuth1(oAuthTwitterConsumerKey, 
													  oAuthTwitterConsumerSecret, 
													  accessToken, 
													  accessTokenSecret, 
													  urlOAuthTwitter);
		
		if( !loginResult.equals("") ) {
			
			joLoginResult 			= JSON.newJSONObject(loginResult);
			
			if( joLoginResult.containsKey("errors") ) {
				
				JSONObject joError 	= JSON.newJSONObject( (JSON.newJSONArray( JSON.get(joLoginResult, "errors") )).get(0).toString() );
				joResult.put("errCode", JSON.get(joError, "code"));
				joResult.put("errMsg", JSON.get(joError, "message"));
				
			}else {
				
				joResult.put("errCode", "00");
				joResult.put("id", JSON.get(joLoginResult, "id_str"));
				joResult.put("customerName", JSON.get(joLoginResult, "name"));				
				joResult.put("email", ( joLoginResult.containsKey("email") ? JSON.get(joLoginResult, "email") : "") );
				if( joLoginResult.containsKey("profile_image_url") ) {
					
					/*Get Filename*/
					fileName 			= util.generateFileName() + ".jpg";
					
					/*Get Photo*/
					util.DownloadImage( JSON.get(joLoginResult, "profile_image_url"), 
										(customerPhotoPath + fileName));
					
					joResult.put("photo", fileName);
					
				}
				
			}
			
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Login with Twitter failed. Please try again later.");
		}
		
		return joResult;
		
	}
	
}
