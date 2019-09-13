package com.webservicemaster.iirs.service.merchant;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface MerchantService {

	public JSONObject authLogin( String uLogin, 
								 String uPassword,
								 String app ) throws ParseException;
	
	public JSONObject authCashier( String username, String password );
	
	public JSONObject getMerchantItem( long merchantId );
	public JSONObject getMerchantItemViaScanBarcode( long merchantId, String itemCode );
}
