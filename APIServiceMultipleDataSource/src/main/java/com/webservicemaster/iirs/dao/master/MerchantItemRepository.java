package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.MerchantItem;

public interface MerchantItemRepository extends BaseRepository<MerchantItem, Long> {

	List<MerchantItem> findByMerchantId( long merchantId );
	
	@Modifying
	@Query("select mi from MerchantItem mi where mi.merchantId = ?1 and mi.merchantItemCategoryId = ?2")
	List<MerchantItem> getMerchantItem( long merchantId,int itemCategoryId );
	
	@Modifying
	@Query("select mi from MerchantItem mi where mi.merchantId = ?1 and mi.itemCode = ?2")
	List<MerchantItem> getMerchantItem( long merchantId, String itemCode );
	
	@Modifying
	@Query("select mi from MerchantItem mi where mi.merchantId = ?1 and mi.itemId = ?2")
	List<MerchantItem> getMerchantItem( long merchantId, long itemId );
	
}
