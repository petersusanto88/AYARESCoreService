package com.webservicemaster.iirs.service.incentivesetting;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.IncentiveSettingRepository;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class IncentiveServiceImpl implements IncentiveService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IncentiveServiceImpl.class);
	private IncentiveSettingRepository incentiveRepo;
	
	@Autowired
	private Utility util;
	
	@Autowired
	public IncentiveServiceImpl( IncentiveSettingRepository repo ) {
		// TODO Auto-generated constructor stub
		incentiveRepo = repo;
	}
	
	@Override
	public JSONObject getIncentiveSettingByIncentiveType( String type ) throws ParseException {
		
		JSONObject joResult = new JSONObject();
		JSONArray jaData = new JSONArray();
		Date now = util.getDate();
		
		List<Object[]> lIncentiveSetting = incentiveRepo.getIncentiveSettingByIncentiveType(type, now);
		if( lIncentiveSetting.size() > 0 ) {
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			
			Iterator<Object[]> it = lIncentiveSetting.iterator();
			while( it.hasNext() ) {
				
				Object[] result = (Object[])it.next();
				JSONObject joData = new JSONObject();
				joData.put("settingName", (String)result[0]);
				joData.put("startDate", ((Date)result[1]).toString());
				joData.put("endDate", ((Date)result[2]).toString());
				joData.put("value1", (String)result[3]);
				joData.put("value2", (String)result[4]);
				jaData.add(joData);
			}
			
			joResult.put("data", jaData);
				
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMesg", "Incentive setting " + type +  " not found");
		}
		
		return joResult;
		
	}
	
}
