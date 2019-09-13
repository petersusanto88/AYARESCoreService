package com.webservicemaster.iirs.service.withdrawl;

import org.json.simple.JSONObject;

public interface WithdrawlService {
	
	public JSONObject transactionWithdrawl( long customerId,
											double amount) throws Exception;
	
	public JSONObject getWithdrawlHistory( long accountId,
										   int start,
										   int limit);

}
