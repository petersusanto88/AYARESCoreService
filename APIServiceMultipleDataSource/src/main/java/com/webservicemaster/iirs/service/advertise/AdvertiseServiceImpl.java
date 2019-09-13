package com.webservicemaster.iirs.service.advertise;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.webservicemaster.iirs.dao.master.AdvertiseRepository;
import com.webservicemaster.iirs.domain.master.Advertise;
import com.webservicemaster.iirs.utility.JSON;

@Service
public class AdvertiseServiceImpl implements AdvertiseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertiseServiceImpl.class);
	private AdvertiseRepository advRepo;
	
	@Value("${advertisement.photo.url}")
	private String advPhotoURL;
	
	@Autowired
	AdvertiseServiceImpl( AdvertiseRepository advRepo ){
		this.advRepo = advRepo;
	}
	
	public JSONObject getAdvertise(){
		
		JSONObject joResult 		= new JSONObject();
		
		List<Advertise> lAdv		= advRepo.getAdvertise( advPhotoURL );
		String jsonAdv 				= new Gson().toJson(lAdv);
		JSONArray jaAdv		 		= JSON.newJSONArray(jsonAdv);
		
		if( jaAdv.size() > 0 ){
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			joResult.put("data", jaAdv);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Data not found");
			
		}
		
		return joResult;
		
	}
	
}
