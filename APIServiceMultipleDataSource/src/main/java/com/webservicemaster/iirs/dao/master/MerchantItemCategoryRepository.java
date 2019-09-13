package com.webservicemaster.iirs.dao.master;

import java.util.List;

import com.webservicemaster.iirs.domain.master.MerchantItemCategory;

public interface MerchantItemCategoryRepository extends BaseRepository<MerchantItemCategory, Integer>{

	List<MerchantItemCategory> findByMerchantId( long merchantId );
	
}
