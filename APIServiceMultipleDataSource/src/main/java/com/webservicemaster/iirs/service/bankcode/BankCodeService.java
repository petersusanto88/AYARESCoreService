package com.webservicemaster.iirs.service.bankcode;

import org.json.simple.JSONArray;

import com.webservicemaster.iirs.domain.master.BankCode;

public interface BankCodeService {

	public JSONArray getBankCodeList();
	public BankCode findByBankCode( String bankCode );
	
}
