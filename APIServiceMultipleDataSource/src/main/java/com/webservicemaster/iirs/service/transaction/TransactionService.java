package com.webservicemaster.iirs.service.transaction;

import java.text.ParseException;

import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

public interface TransactionService {
	
	public JSONObject transactionOnMerchantModel1( String qrCode,
												   int userId,
												   String merchantTransactionNumber,
												   double totalPurchase);
	
	public JSONObject transactionOnMerchantModel2( int userId,
												   String sessionToken,
												   JSONObject joParam );
	
	public JSONObject transactionRedeem( String voucherCode,
										 int userId,
										 String merchantTransactionNumber ) throws ParseException;
	
	public JSONObject transactionUsingCustomerApps( int userId, 
													String merchantId,
													String cashierUsername,
													String cashierPassword,
													double total )throws Exception;
	
	public void getIncentiveAfterRegister( long customerId ) throws ParseException;
	
	
	
//	public JSONObject getTransactionHistory( long accountId, String keyword );
	
}
