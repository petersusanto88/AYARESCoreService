package com.webservicemaster.iirs.service.account;

import java.util.List;

import org.json.simple.JSONObject;

import com.webservicemaster.iirs.domain.master.Account;

public interface AccountService {

	public JSONObject addAccount( String accountCategory, long customerId );
	public boolean validateAccountNumber( String accountNumber );
	public List<Account> getAccountDataByAccountNumber( String accountNumber );
	public Account getAccountDataByMemberId( long memberId );
	public Account getCustomerIdByAccountNumber( String accountNumber );
	
}
