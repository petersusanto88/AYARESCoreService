package com.webservicemaster.iirs.service.merchantuseraccess;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface MerchantUserAccessService {

	public JSONObject authenticationHeaderRequest( String token, 
			   									   int userId ) throws ParseException;

	public int setSessionToken( String sessionToken, int customerId ) throws ParseException;
	
	public JSONObject getMerchantInfoByUserId( int userId );
	
}
