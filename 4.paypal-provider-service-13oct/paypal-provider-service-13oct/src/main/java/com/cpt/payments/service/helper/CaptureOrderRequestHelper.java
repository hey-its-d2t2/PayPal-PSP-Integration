package com.cpt.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.service.TokenService;
import com.google.gson.Gson;

@Component
public class CaptureOrderRequestHelper {
	
	@Value("${paypal.clientid}")
	private String clientId;

	@Value("${paypal.clientsecret}")
	private String clientSecret;

	@Value("${paypal.captureOrder.url}")
	private String captureOrderUrl;
	
	private Gson gson;

	private TokenService tokenService;
	
	public CaptureOrderRequestHelper(TokenService tokenService, Gson gson) {
		this.tokenService = tokenService;
		this.gson = gson;
	}

	public HttpRequest prepareRequest(String orderId) {
		System.out.println("CaptureOrderRequestHelper preparing Request "
				+ "orderId:" + orderId);
		
		HttpRequest httpRequest = new HttpRequest();

		captureOrderUrl = captureOrderUrl.replace("{id}", orderId);
		
		httpRequest.setUrl(captureOrderUrl);
		httpRequest.setMethod(HttpMethod.POST);

		HttpHeaders headers = new HttpHeaders();
		
		String accessToken = tokenService.getAccessToken();
		System.out.println("Got access token: " + accessToken);
		
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);


		httpRequest.setHttpHeaders(headers);

		String reqAsJson = null;
		
		httpRequest.setRequest(reqAsJson);
		
		System.out.println("CaptureOrderRequestHelper:: Prepared Request:" + httpRequest);
		
		return httpRequest;
	}

}
