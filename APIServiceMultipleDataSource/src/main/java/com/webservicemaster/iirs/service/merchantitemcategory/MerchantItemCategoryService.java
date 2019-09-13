package com.webservicemaster.iirs.service.merchantitemcategory;

import java.util.List;

import com.webservicemaster.iirs.domain.master.MerchantItemCategory;

public interface MerchantItemCategoryService {

	public List<MerchantItemCategory> getItemCategoryByMerchantId( long merchantId ); 
	
}
