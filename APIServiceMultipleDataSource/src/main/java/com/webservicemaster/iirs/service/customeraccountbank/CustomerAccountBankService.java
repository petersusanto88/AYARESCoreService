package com.webservicemaster.iirs.service.customeraccountbank;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.webservicemaster.iirs.domain.master.CustomerAccountBank;

public interface CustomerAccountBankService {

	public JSONObject updateAccountBankSetting( long customerId,
											    String bankCode,
											    String accountNumber,
											    String accountName,
											    String identityCard,
											    String fileIdentityCard);	
	
	public JSONObject deleteAccountBankSetting( long customerId );
	
	public JSONObject uploadIdentityCard( MultipartFile file );
	
	public CustomerAccountBank findByCustomerId( long customerId );
}
