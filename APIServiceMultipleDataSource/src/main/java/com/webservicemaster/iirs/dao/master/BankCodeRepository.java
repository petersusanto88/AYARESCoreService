package com.webservicemaster.iirs.dao.master;

import java.util.List;

import com.webservicemaster.iirs.domain.master.BankCode;

public interface BankCodeRepository extends BaseRepository<BankCode, String> {

	List<BankCode> findAll();
	
	BankCode findByBankCode( String bankCode );
	
}
