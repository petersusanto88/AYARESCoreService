package com.webservicemaster.iirs.service.evoucher;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.EVoucherRepository;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class EVoucherServiceImpl implements EVoucherService{

	private static final Logger LOGGER = LoggerFactory.getLogger(EVoucherServiceImpl.class);
	private EVoucherRepository eVoucherRepo;
	
	@Autowired
	private Utility util;
	
	@Autowired
	public EVoucherServiceImpl( EVoucherRepository repo ) {
		this.eVoucherRepo = repo;
	}
	
	public JSONObject getEVoucher( Short isAutoCampaignAfterRegister, String validationPrefixUsername ) throws ParseException {
		
		JSONObject joResult = new JSONObject();
		JSONObject joData = null;
		JSONArray jaData = new JSONArray();
		
		List<Object[]> lVoucher = eVoucherRepo.getEVoucherAutoCampaign(util.getDate(), isAutoCampaignAfterRegister, validationPrefixUsername );
		if( lVoucher.size() > 0 ) {
			Iterator<Object[]> it = lVoucher.iterator();
			while( it.hasNext() ) {
				Object[] result = (Object[])it.next();
				joData = new JSONObject();
				joData.put("voucherId", (Long)result[0]);
				joData.put("merchantId", (Long)result[1]);
				joData.put("voucherValue", (Double)result[2]);
				joData.put("validationPrefixUsername", (String)result[3] );
				jaData.add(joData);
			}
			joResult.put("errCode", "00");
			joResult.put("data", jaData);
		}else {
			joResult.put("errCode", "-99");
		}
		
		return joResult;
		
	}
	
}
