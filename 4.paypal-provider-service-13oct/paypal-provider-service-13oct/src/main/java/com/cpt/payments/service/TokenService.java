package com.cpt.payments.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cpt.payments.constant.Constant;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypal.TokenResponse;
import com.google.gson.Gson;

@Service
public class TokenService {
	
	private HttpClientUtil httpClientUtil;
	
	private Gson gson;
	
	private static String accessToken;
	
	@Value("${paypal.clientid}")
	private String clientId;
	
	@Value("${paypal.clientsecret}")
	private String clientSecret;
	
	@Value("${paypal.outh.url}")
	private String oauthUrl;
	
	public TokenService(HttpClientUtil httpClientUtil, Gson gson) {
		this.httpClientUtil = httpClientUtil;
		this.gson = gson;
	}
	
	public String getAccessToken() {
		
		//TODO, this static caching is for temporary purpose, 
		//we will implement final solution in redis, which deals with expiry aswell.
		
		if (accessToken != null) {
			System.out.println("Returning already available accessToken");
			return accessToken;
		}
		
		System.out.println("accessToken is null, so making OAuth call to get accessToken");
		
		//"https://api-m.sandbox.paypal.com/v1/oauth2/token"
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setUrl(oauthUrl);
		
		httpRequest.setMethod(HttpMethod.POST);
		
		MultiValueMap<String, String> requestPayload = new LinkedMultiValueMap<>();
		requestPayload.add(Constant.OAUTH_GRANT_TYPE, Constant.OAUTH_GRANT_CLIENT_CREDENTIALS);
		httpRequest.setRequest(requestPayload);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.setBasicAuth(clientId, clientSecret);
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		httpRequest.setHttpHeaders(httpHeaders);
		
		System.out.println("TokenService:: Making HTTP Call for httpRequest:" + httpRequest);

		ResponseEntity<String> outhTokenResponse = httpClientUtil.makeHttpRequest(
				httpRequest);
		
		String resBodyAsJson = outhTokenResponse.getBody();
		
		System.out.println("Got outhTokenResponse: " + outhTokenResponse);
		System.out.println("Got resBodyAsJson: " + resBodyAsJson);
		
		TokenResponse responseAsObj = gson.fromJson(
				resBodyAsJson, TokenResponse.class);

		System.out.println("TokenResponse as Java Object responseAsObj: " + responseAsObj);
		
		accessToken = responseAsObj.getAccessToken();
		
		System.out.println("Returning accessToken: " + accessToken);
		
		return accessToken;
	}

}
