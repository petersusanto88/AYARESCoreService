package com.webservicemaster.iirs.service.merchantitem;

import java.util.List;

import org.json.simple.JSONObject;

import com.webservicemaster.iirs.domain.master.MerchantItem;


public interface MerchantItemService {

	public List<MerchantItem> getMerchantItem( long merchantId, int itemCategoryId );
	public JSONObject getMerchantItemViaScanBarcode( long merchantId, String itemCode );
	public JSONObject getMerchantItem( long merchantId, long itemId );
}
