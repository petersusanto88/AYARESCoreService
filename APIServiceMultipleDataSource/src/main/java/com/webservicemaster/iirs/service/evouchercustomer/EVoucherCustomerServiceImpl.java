package com.webservicemaster.iirs.service.evouchercustomer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.EVoucherCustomerRepository;
import com.webservicemaster.iirs.domain.master.EVoucherCustomer;

@Service
public class EVoucherCustomerServiceImpl implements EVoucherCustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EVoucherCustomerServiceImpl.class);
	private EVoucherCustomerRepository eVoucherRepo;
	
	@Autowired
	EVoucherCustomerServiceImpl(EVoucherCustomerRepository repository) {
        this.eVoucherRepo = repository;
    }
	
	@Override
	public boolean validateEVoucherCode( String voucherCode,
										 int userId ){
		
		boolean flag	= false;
		
		List lValidate	= eVoucherRepo.validateEVoucherCustomer(voucherCode, 
																userId);
		
		if( Integer.parseInt( lValidate.get(0).toString() ) > 0 ){
			
			flag 		= true;
			
		}
		
		return flag;
		
	}
	
	@Override
	public EVoucherCustomer findByVoucherCode( String voucherCode ){
		
		return eVoucherRepo.findByVoucherCode(voucherCode);
		
	}
	
}
