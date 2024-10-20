package com.cpt.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypalprovider.Order;
import com.cpt.payments.service.helper.PaypalProviderGetOrderRequestHelper;
import com.cpt.payments.service.interfaces.ProviderHandler;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaypalProviderHandler implements ProviderHandler {
	
	private HttpClientUtil httpClientUtil;
	
	private PaypalProviderGetOrderRequestHelper paypalProviderGetOrderRequestHelper;
	
	private Gson gson;
	
	public PaypalProviderHandler(HttpClientUtil httpClientUtil, 
			PaypalProviderGetOrderRequestHelper paypalProviderGetOrderRequestHelper, 
			Gson gson) {
		this.httpClientUtil = httpClientUtil;
		this.paypalProviderGetOrderRequestHelper = paypalProviderGetOrderRequestHelper;
		this.gson = gson;
	}

	@Override
	public void reconcileTransaction(TransactionDTO transactionDTO) {
		log.info("reconcileTransaction() called. transactionDTO:" + transactionDTO);
		
		//Prepare HttpRequest to call Paypal provider for getstatus call.
		// Invoke HttpClientUtil makeHttpRequest() method.
		// get the response. & get body of response.
		// Parse the response body to get status using Gson
		
		HttpRequest httpRequest = paypalProviderGetOrderRequestHelper.prepareRequest(
				transactionDTO.getProviderReference());
		
		ResponseEntity<String> response = httpClientUtil.makeHttpRequest(httpRequest);
		
		String responseBody = response.getBody();
		
		log.info("responseBody: " + responseBody);
		
		Order orderObj = gson.fromJson(responseBody, Order.class);
		
		log.info("orderObj: " + orderObj);
	}

}
