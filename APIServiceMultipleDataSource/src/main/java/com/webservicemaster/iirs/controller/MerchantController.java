package com.webservicemaster.iirs.controller;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.service.merchant.MerchantService;
import com.webservicemaster.iirs.service.merchantuseraccess.MerchantUserAccessService;
import com.webservicemaster.iirs.utility.JSON;

@RestController
@RequestMapping("/api.mobile/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private MerchantUserAccessService muaService;
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String doLogin( @RequestBody String jsonParam ) throws ParseException{
		
		System.out.println("TEST 123");
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joResultLogin 	= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( jsonParam );
		Logger logger 				= LoggerFactory.getLogger("");
		
		logger.debug("[doLogin] ### START ###");
		logger.debug("[doLogin] HEADER : -");
		logger.debug("[doLogin] BODY : " + jsonParam);
		logger.debug("[doLogin] ### END ###");
		
		joResultLogin 				= merchantService.authLogin( JSON.get(joParam, "userLogin"), 
																 JSON.get(joParam, "userPassword"),
																 JSON.get(joParam, "app"));
		
		return joResultLogin.toString();
		
	}
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMerchantItem", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMerchantItem( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
												 @RequestHeader(value="X-ID") int userId,
												 @RequestBody String jsonParam ) throws ParseException{
		
		JSONObject joResult 			= new JSONObject();		
		JSONObject joResultGetMerchant 	= new JSONObject();		
		JSONObject joParam 				= JSON.newJSONObject( jsonParam );
		
		Logger logger 					= LoggerFactory.getLogger("");		
		logger.debug("[updatePassword] ### START ###");
		logger.debug("[updatePassword] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updatePassword] BODY : " + jsonParam);
		logger.debug("[updatePassword] ### END ###");
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		System.out.println("User ID : " + userId);
		joTokenStatus				= muaService.authenticationHeaderRequest(sessionToken, userId);
		long merchantId 			= Long.parseLong( JSON.get(joParam, "merchantId") );
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){
			
			joResult 				= merchantService.getMerchantItem(merchantId);
			
		}else{
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));
		}
		
		return joResult.toString();
		
	}
	
	
	@CrossOrigin(origins="*")
	@RequestMapping(value="/getMerchantItemViaScanBarcode", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String getMerchantItemViaScanBarcode( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
												 			   @RequestHeader(value="X-ID") int userId,
												 			   @RequestBody String jsonParam ) throws ParseException{
		
		JSONObject joResult 			= new JSONObject();		
		JSONObject joResultGetMerchant 	= new JSONObject();		
		JSONObject joParam 				= JSON.newJSONObject( jsonParam );
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= muaService.authenticationHeaderRequest(sessionToken, userId);
		long merchantId 			= Long.parseLong( JSON.get(joParam, "merchantId") );
		String itemCode 			= JSON.get(joParam, "itemCode");
		
		Logger logger 				= LoggerFactory.getLogger("");		
		logger.debug("[updatePassword] ### START ###");
		logger.debug("[updatePassword] HEADER : X-ID = " + userId + ", X-SESSION-TOKEN = " + sessionToken);
		logger.debug("[updatePassword] BODY : " + jsonParam);
		logger.debug("[updatePassword] ### END ###");
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){
			
			joResult 				= merchantService.getMerchantItemViaScanBarcode(merchantId, itemCode);
			
		}else{
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));
		}
		
		return joResult.toString();
		
	}
	
}
