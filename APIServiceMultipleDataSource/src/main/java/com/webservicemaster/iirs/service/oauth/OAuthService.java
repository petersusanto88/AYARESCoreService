package com.webservicemaster.iirs.service.oauth;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.intuit.ipp.exception.FMSException;

public interface OAuthService {

	public JSONObject loginViaFB( String accessToken ) throws Exception;
	public JSONObject loginViaGoogle( String accessToken ) throws IOException;
	public JSONObject loginViaTwitter( String accessToken, String accessTokenSecret ) throws FMSException, IOException;
}
