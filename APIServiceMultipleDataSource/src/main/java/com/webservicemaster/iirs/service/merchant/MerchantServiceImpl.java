package com.webservicemaster.iirs.service.merchant;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.MerchantRepository;
import com.webservicemaster.iirs.domain.master.MerchantItem;
import com.webservicemaster.iirs.domain.master.MerchantItemCategory;
import com.webservicemaster.iirs.service.merchantitem.MerchantItemService;
import com.webservicemaster.iirs.service.merchantitemcategory.MerchantItemCategoryService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.EncryptorUtility;
import com.webservicemaster.iirs.utility.JSON;
import com.webservicemaster.iirs.utility.SessionIdGenerator;
import com.webservicemaster.iirs.utility.Utility;
import com.google.gson.Gson;

@Service
public class MerchantServiceImpl implements MerchantService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantServiceImpl.class);
	private MerchantRepository merchantRepo;
	
	@Value("${db.key.md5}")
	private String dbMD5Key;
	
	@Value("${session.interval.timeout}")
	private int timeout;
	
	@Value("${aes.key}")
	private String aesKey;
	
	@Autowired
	EncryptorUtility encUtil;
	
	@Autowired
	private Utility util;
	
	@Autowired
	SessionIdGenerator sessionGenerator;
	
	@Autowired
	private MerchantItemCategoryService itemCategory;
	
	@Autowired
	private MerchantItemService itemService;
	
	@Autowired
	MerchantServiceImpl(MerchantRepository repository) {
        this.merchantRepo = repository;
    }	
	
	@Transactional
	@Override
	public JSONObject authLogin( String uLogin, 
								 String uPassword,
								 String app ) throws ParseException{
		
		JSONObject joAuth 		 = new JSONObject();
		JSONObject joData 		 = new JSONObject();
		String sessionToken      = "";
		Date tokenExpired 		 = null;
		
		System.out.println((encUtil.MD5Hashing( uPassword, dbMD5Key ) ).toUpperCase());
		
		List<Object[]> lAuth 			 	 = merchantRepo.authLogin( uLogin, 
								       		   			   			   (encUtil.MD5Hashing( uPassword, dbMD5Key ) ).toUpperCase() );
		
		
		if( lAuth.size() > 0 ){
			
			for( Object[] result : lAuth ){
				
				sessionToken		 = sessionGenerator.generateId(32);
				tokenExpired 		 = util.addingMinutes(timeout);			
				
				// Set session Token
				merchantRepo.setSessionToken(sessionToken, tokenExpired, (Integer)result[0] );	
				
				joData.put("userId", (Integer)result[0] );
				joData.put("userName", (String)result[1] );
				joData.put("merchantId", (Long)result[5]);
				joData.put("merchantName", (String)result[2] );
				joData.put("logo",(String)result[3] );
				joData.put("branchName", (String)result[4]);
				joData.put("sessionToken", sessionToken);
				
				joAuth.put("errCode", "00");
				joAuth.put("errMsg", "OK");
				joAuth.put("data", joData);
				
			}			
			
		}else{
			
			joAuth.put("errCode", "-99");
			joAuth.put("errMsg", "Login failed");
			
		}
		
		return joAuth;
	}
	
	@Override
	public JSONObject getMerchantItem( long merchantId ){
		
		JSONObject joResult								= new JSONObject();
		int itemCategoryId 								= 0;
		JSONArray jaResultItemCategory 					= new JSONArray();
		JSONObject joResultItemCategory 				= new JSONObject();
		JSONArray jaResultItem 							= new JSONArray();
		JSONObject joResultItem 						= new JSONObject();
		
		/*1. Get Merchant Category*/
		List<MerchantItemCategory> merchantItemCategory = itemCategory.getItemCategoryByMerchantId(merchantId);
		String gsonMerchantItemCategory 				= new Gson().toJson( merchantItemCategory );
		JSONArray jaItemCategory 						= JSON.newJSONArray(gsonMerchantItemCategory);
		
		if( jaItemCategory.size() > 0 ){
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			
			for( int i=0; i < jaItemCategory.size(); i++ ){
				
				joResultItemCategory 					= new JSONObject();
				joResultItem 							= new JSONObject();
				
				JSONObject joItemCategory 				= JSON.newJSONObject( jaItemCategory.get(i).toString() );
				itemCategoryId 							= JSON.getInt(joItemCategory, "itemCategoryId");				
				List<MerchantItem> listItem 			= itemService.getMerchantItem(merchantId, itemCategoryId);				
				String gsonMerchantItem 				= new Gson().toJson( listItem );
				
				JSONArray jaMerchantItem				= JSON.newJSONArray( gsonMerchantItem );
				JSONObject joMerchantItem 				= null;
				
				joResultItemCategory 					= joItemCategory;				
				
				joResultItemCategory.put("items", jaMerchantItem);
				
				jaResultItemCategory.add(joResultItemCategory);
				
			}
			
			joResult.put("data", jaResultItemCategory);
			
		}else{
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "Merchant item cagories not found");
			joResult.put("data",jaResultItemCategory);
			
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getMerchantItemViaScanBarcode( long merchantId, String itemCode ){
		
		return itemService.getMerchantItemViaScanBarcode(merchantId, itemCode);
		
	}
	
	@Override
	public JSONObject authCashier( String username, String password ) {
		
		JSONObject joResult 			= new JSONObject();
		
		List<Object[]> lCashier 		= merchantRepo.authLogin(username, password);
		if( lCashier.size() > 0 ) {
			
			joResult.put("errCode", "00");
			
			Iterator<Object[]> it		= lCashier.iterator();
			
			while( it.hasNext() ) {
				
				Object[] result 		= (Object[])it.next();
				joResult.put("userId", (int)result[0]);
				joResult.put("username", (String)result[1]);
				joResult.put("merchantId", (long)result[5]);
				
			}
			
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Cashier ID or Password not valid");
		}
		
		return joResult;
		
	}
	
	
}
