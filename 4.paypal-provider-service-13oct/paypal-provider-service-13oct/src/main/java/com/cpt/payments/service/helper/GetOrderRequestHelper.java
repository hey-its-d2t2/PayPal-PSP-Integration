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
public class GetOrderRequestHelper {
	
	@Value("${paypal.clientid}")
	private String clientId;

	@Value("${paypal.clientsecret}")
	private String clientSecret;

	@Value("${paypal.getOrder.url}")
	private String getOrderUrl;
	
	private Gson gson;

	private TokenService tokenService;
	
	public GetOrderRequestHelper(TokenService tokenService, Gson gson) {
		this.tokenService = tokenService;
		this.gson = gson;
	}

	public HttpRequest prepareRequest(String orderId) {
		System.out.println("GetOrderRequestHelper preparing Request "
				+ "orderId:" + orderId);
		
		HttpRequest httpRequest = new HttpRequest();

		String url = new String(getOrderUrl);
		url = url.replace("{id}", orderId);
		
		httpRequest.setUrl(url);
		httpRequest.setMethod(HttpMethod.GET);

		HttpHeaders headers = new HttpHeaders();
		
		String accessToken = tokenService.getAccessToken();
		System.out.println("Got access token: " + accessToken);
		
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);


		httpRequest.setHttpHeaders(headers);

		String reqAsJson = null;
		
		httpRequest.setRequest(reqAsJson);
		
		System.out.println("GetOrderRequestHelper:: Prepared Request:" + httpRequest);
		
		return httpRequest;
	}

}
