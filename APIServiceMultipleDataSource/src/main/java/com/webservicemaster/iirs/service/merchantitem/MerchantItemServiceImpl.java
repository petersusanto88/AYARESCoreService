package com.webservicemaster.iirs.service.merchantitem;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.webservicemaster.iirs.dao.master.MerchantItemRepository;
import com.webservicemaster.iirs.domain.master.MerchantItem;

import com.webservicemaster.iirs.utility.JSON;

@Service
public class MerchantItemServiceImpl implements MerchantItemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantItemServiceImpl.class);
	private MerchantItemRepository merchantItemRepo;
	
	@Autowired
	MerchantItemServiceImpl( MerchantItemRepository merchantItemRepo ){
		this.merchantItemRepo = merchantItemRepo;
	}
	
	@Override
	public List<MerchantItem> getMerchantItem( long merchantId, int itemCategoryId ){
		
		return merchantItemRepo.getMerchantItem(merchantId, itemCategoryId);
		
	}
	
	@Override
	public JSONObject getMerchantItemViaScanBarcode( long merchantId, String itemCode ){
		
		JSONObject joResult					= new JSONObject();
		
		List<MerchantItem> listItem 		= merchantItemRepo.getMerchantItem(merchantId, itemCode);
		String gsonMerchantItem 			= new Gson().toJson( listItem );
		JSONArray jaMerchantItem 			= JSON.newJSONArray( gsonMerchantItem );
		
		if( jaMerchantItem.size() > 0 ){
			
			JSONObject joItem 			 	= JSON.newJSONObject( jaMerchantItem.get(0).toString() );
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			joResult.put("data", joItem);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
		}
		
		return joResult;
		
	}
	
	@Override
	public JSONObject getMerchantItem( long merchantId, long itemId ){
		
		JSONObject joResult 				= new JSONObject();
		List<MerchantItem> listItem 		= merchantItemRepo.getMerchantItem(merchantId, itemId);
		String gsonMerchantItem 			= new Gson().toJson( listItem );
		JSONArray jaMerchantItem 			= JSON.newJSONArray( gsonMerchantItem );
		
		if( jaMerchantItem.size() > 0 ){
			
			JSONObject joItem 			 	= JSON.newJSONObject( jaMerchantItem.get(0).toString() );
			joResult.put("errCode", "00");
			joResult.put("itemId", JSON.get(joItem, "itemId"));
			joResult.put("rebate", JSON.get(joItem, "rebate"));
			
		}else{
			joResult.put("errCode", "-99");
		}
		
		return joResult;
	}
	
	
}
