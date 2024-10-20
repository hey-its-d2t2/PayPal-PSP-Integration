package com.cpt.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cpt.payments.http.HttpRequest;

@Component
public class PaypalProviderCaptureOrderRequestHelper {
	
	@Value("${paypal-provider.captureOrder.url}")
	private String captureOrderUrl;
	
	public HttpRequest prepareRequest(String orderId) {
		System.out.println("CaptureOrderRequestHelper preparing Request "
				+ "orderId:" + orderId);
		
		HttpRequest httpRequest = new HttpRequest();

		captureOrderUrl = captureOrderUrl.replace("{providerReference}", orderId);
		
		httpRequest.setUrl(captureOrderUrl);
		httpRequest.setMethod(HttpMethod.POST);

		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		httpRequest.setHttpHeaders(headers);

		String reqAsJson = null;
		
		httpRequest.setRequest(reqAsJson);
		
		System.out.println("CaptureOrderRequestHelper:: Prepared Request:" + httpRequest);
		
		return httpRequest;
	}

}
