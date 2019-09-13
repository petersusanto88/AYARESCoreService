package com.webservicemaster.iirs.controller;

import java.text.ParseException;
import java.util.Date;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webservicemaster.iirs.service.customer.CustomerService;
import com.webservicemaster.iirs.service.merchantuseraccess.MerchantUserAccessService;
import com.webservicemaster.iirs.service.transaction.TransactionService;
import com.webservicemaster.iirs.service.transactiondetail.TransactionDetailService;
import com.webservicemaster.iirs.service.withdrawl.WithdrawlService;
import com.webservicemaster.iirs.utility.Utility;
import com.webservicemaster.iirs.utility.JSON;

@RestController
@RequestMapping( "/api.mobile/merchant/transaction/" )
public class TransactionController {
	
	@Autowired
	private MerchantUserAccessService muaService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private TransactionService transService;
	
	@Autowired
	private TransactionDetailService transDetailService;
	
	@Autowired
	private Utility util;
	
	
	
	//private MobileAPILog mobileAPILog = new MobileAPILog();
	
	@RequestMapping( value="/transactionOnMerchantModel1", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String transactionOnMerchantModel1( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
															 @RequestHeader(value="X-ID") int userId,
															 @RequestBody String param ) throws ParseException{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		String qrCode 				= JSON.get(joParam, "qrCode");
		
		if( qrCode.toLowerCase().contains("ayares") ) {
			qrCode = qrCode.replace("ayares", "");
		}
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= muaService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
			
			System.out.println("### QRCODE : " + JSON.get(joParam, "qrCode"));
		
			joResult 					= transService.transactionOnMerchantModel1(qrCode, 
																				   JSON.getInt(joParam, "userId"), 
																				   JSON.get(joParam, "merchantTransactionNumber"),
																				   Double.parseDouble( JSON.get(joParam, "totalPurchase") ));	
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
			
		return joResult.toString();
		
	}

	@RequestMapping( value="/transactionOnMerchantModel2", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String transactionOnMerchantModel2( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
															 @RequestHeader(value="X-ID") int userId,
															 @RequestBody String param ) throws ParseException{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= muaService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
		
			joResult 					= transService.transactionOnMerchantModel2(userId, 
																				   sessionToken,
																				   joParam);	
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("tokenStatus", JSON.getInt(joTokenStatus, "tokenStatus"));
			joResult.put("tokenMsg", JSON.get(joTokenStatus, "tokenMsg"));	
			joResult.put("errMsg", JSON.get(joTokenStatus, "tokenMsg"));
			
		}
		/*End :Verify the header*/
			
		return joResult.toString();
		
	}
	
	
	@RequestMapping( value="/redeemVoucher", method=RequestMethod.POST, produces="application/json" )
	public @ResponseBody String redeemVoucher( @RequestHeader(value="X-SESSION-TOKEN") String sessionToken,
											   @RequestHeader(value="X-ID") int userId,
											   @RequestBody String param ) throws ParseException{
		
		JSONObject joResult 		= new JSONObject();		
		JSONObject joParam 			= JSON.newJSONObject( param );
		
		/*1. Verify the header*/
		JSONObject joTokenStatus 	= new JSONObject();		
		joTokenStatus				= muaService.authenticationHeaderRequest(sessionToken, userId);
		
		if( JSON.getInt(joTokenStatus, "tokenStatus") == 1 ){			
		
			joResult 					= transService.transactionRedeem(JSON.get(joParam, "voucherCode"), 
																		 userId, 
																		 JSON.get(joParam, "merchantTransactionNumber"));
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
