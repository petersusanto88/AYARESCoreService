package com.webservicemaster.iirs.service.merchantitemcategory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.MerchantItemCategoryRepository;
import com.webservicemaster.iirs.domain.master.MerchantItemCategory;

@Service
public class MerchantItemCategoryServiceImpl implements MerchantItemCategoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantItemCategoryServiceImpl.class);
	private MerchantItemCategoryRepository itemCategoryRepo;
	
	@Autowired
	MerchantItemCategoryServiceImpl( MerchantItemCategoryRepository itemCategoryRepo ) {
		this.itemCategoryRepo = itemCategoryRepo;
	}
	
	public List<MerchantItemCategory> getItemCategoryByMerchantId( long merchantId ){
		
		return itemCategoryRepo.findByMerchantId(merchantId);
		
	}
	
}
