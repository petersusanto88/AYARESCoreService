package com.webservicemaster.iirs.service.transactiondetail;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface TransactionDetailService {
	
	public JSONObject getTransactionHistory( long accountId, 
										 	 String keyword,
										 	 int start,
										 	 int limit);
	
	public JSONObject getTransactionDetailByTransactionNumber( String transactionNumber, long accountId );
	

}
