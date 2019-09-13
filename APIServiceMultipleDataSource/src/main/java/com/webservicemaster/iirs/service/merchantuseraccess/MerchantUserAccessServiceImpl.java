package com.webservicemaster.iirs.service.merchantuseraccess;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.MerchantUserAccessRepository;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class MerchantUserAccessServiceImpl implements MerchantUserAccessService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantUserAccessServiceImpl.class);
	private MerchantUserAccessRepository muaRepo;
	
	@Autowired
	MerchantUserAccessServiceImpl(MerchantUserAccessRepository repository) {
        this.muaRepo = repository;
    }
	
	@Autowired
	private Utility util;
	
	@Value("${session.interval.timeout}")
	private int timeout;
	
	@Transactional
	@Override
	public JSONObject authenticationHeaderRequest( String token, int userId ) throws ParseException{
		
		/* 1	=>token and userId valid
		 * -1	=>token not valid
		 * -2	=>token expired*/
		
		JSONObject jo 		= new JSONObject();
		List l1 			= muaRepo.tokenAvailableByCustomerId(token, userId);
		List l2 			= muaRepo.isTokenExpired( util.getDateTime(), userId );
		
		if( Integer.parseInt(l1.get(0).toString()) > 0 ){
			
			if( Integer.parseInt(l2.get(0).toString()) == 0 ){
				
				/*Update token expired*/
				
				this.setSessionToken(token, userId);
				
				jo.put("tokenStatus", 1);
				jo.put("tokenMsg", "Token Valid");
				
			}else{
				
				jo.put("tokenStatus", -2);
				jo.put("tokenMsg", "Token Expired");
				
			}
			
		}else{
			
			jo.put("tokenStatus", -1);
			jo.put("tokenMsg", "Token not valid");
			
		}	
		
		return jo;
		
	}
	
	@Transactional
	@Override
	public int setSessionToken( String sessionToken, int userId ) throws ParseException{
		
		Date tokenExpired = util.addingMinutes(timeout);
		
		return muaRepo.setSessionToken(sessionToken, tokenExpired, userId);
		
	}
	
	@Override
	public JSONObject getMerchantInfoByUserId( int userId ) {
		
		JSONObject joResult 		= new JSONObject();
		
		List<Object[]> lMerchant 	= muaRepo.getMerchantInfoByUserId(userId);
		
		if( lMerchant.size() > 0 ) {
			
			Iterator<Object[]> it 	= lMerchant.iterator();
			
			while( it.hasNext() ) {
				
				Object[] result 	= (Object[])it.next();
				
				joResult.put("icon", (String)result[1]);
				joResult.put("merchantName", (String)result[0]);
				joResult.put("merchantAddress", (String)result[2]);
				joResult.put("merchantLatitude", ((String)result[3]));
				joResult.put("merchantLongitude", ((String)result[4]));
				
			}
		}else {
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Merchant not found");
		}
		
		return joResult;
		
	}
	
}
