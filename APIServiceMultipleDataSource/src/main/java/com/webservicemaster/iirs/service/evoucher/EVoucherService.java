package com.webservicemaster.iirs.service.evoucher;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface EVoucherService {

	public JSONObject getEVoucher( Short isAutoCampaignAfterRegister, String validationPrefixUsername )  throws ParseException;
	
}
