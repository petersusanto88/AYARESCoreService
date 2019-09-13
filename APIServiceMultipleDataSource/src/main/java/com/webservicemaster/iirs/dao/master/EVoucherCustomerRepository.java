package com.webservicemaster.iirs.dao.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.webservicemaster.iirs.domain.master.EVoucherCustomer;

public interface EVoucherCustomerRepository extends BaseRepository<EVoucherCustomer, Long> {

	
	@Modifying
	@Query( " select count(evc) "
		  + " from EVoucherCustomer evc, EVoucherProduct evp, MerchantUserAccess mua"
		  + " where evc.voucherId = evp.voucherId"
		  + " and evp.merchantId = mua.merchantId"
		  + " and evc.voucherCode = ?1"
		  + " and mua.userId = ?2" )
	public List validateEVoucherCustomer( String voucherCode, int userId);	
	public EVoucherCustomer findByVoucherCode( String voucherCode );
	EVoucherCustomer save( EVoucherCustomer data );
	void flush();
	
}
