package com.webservicemaster.iirs.service.evouchercustomer;

import com.webservicemaster.iirs.domain.master.EVoucherCustomer;

public interface EVoucherCustomerService {

	public boolean validateEVoucherCode( String voucherCode, int userId );	
	public EVoucherCustomer findByVoucherCode( String voucherCode );
	
}
