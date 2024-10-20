package com.cpt.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cpt.payments.http.HttpRequest;

@Component
public class PaypalProviderGetOrderRequestHelper {
	
	@Value("${paypal-provider.getOrder.url}")
	private String getOrderUrl;
	
	public HttpRequest prepareRequest(String providerReference) {
		System.out.println("GetOrderRequestHelper preparing Request "
				+ "orderId:" + providerReference);
		
		HttpRequest httpRequest = new HttpRequest();
		//http://localhost:8083/v1/paypal/order/{providerReference}
		
		String url = new String(getOrderUrl);
		url = url.replace("{providerReference}", providerReference); 
		
		httpRequest.setUrl(url);
		httpRequest.setMethod(HttpMethod.GET);

		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		httpRequest.setHttpHeaders(headers);

		String reqAsJson = null;
		
		httpRequest.setRequest(reqAsJson);
		
		System.out.println("GetOrderRequestHelper:: Prepared Request:" + httpRequest);
		
		return httpRequest;
	}

}
