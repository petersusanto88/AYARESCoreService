package com.webservicemaster.iirs.service.incentivesetting;

import java.text.ParseException;

import org.json.simple.JSONObject;

public interface IncentiveService {
	public JSONObject getIncentiveSettingByIncentiveType( String type ) throws ParseException;	
}
