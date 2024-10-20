package com.cpt.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpt.payments.constant.Constant;
import com.cpt.payments.dto.CreateOrderReqDTO;
import com.cpt.payments.dto.OrderDTO;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypal.res.Link;
import com.cpt.payments.paypal.res.OrderResponse;
import com.cpt.payments.service.helper.CaptureOrderRequestHelper;
import com.cpt.payments.service.helper.CreateOrderRequestHelper;
import com.cpt.payments.service.helper.GetOrderRequestHelper;
import com.cpt.payments.service.interfaces.PaymentService;
import com.google.gson.Gson;

@Service
public class PaymentServiceImpl implements PaymentService {

	private CreateOrderRequestHelper createOrderRequestHelper;

	private GetOrderRequestHelper getOrderRequestHelper;
	
	private CaptureOrderRequestHelper captureOrderRequestHelper;

	private HttpClientUtil httpClientUtil;

	private Gson gson;

	public PaymentServiceImpl(Gson gson, HttpClientUtil httpClientUtil, 
			CreateOrderRequestHelper createOrderRequestProcessor, 
			GetOrderRequestHelper getOrderRequestProcessor,
			CaptureOrderRequestHelper captureOrderRequestProcessor) {
		this.gson = gson;
		this.httpClientUtil = httpClientUtil;
		this.createOrderRequestHelper = createOrderRequestProcessor;
		this.getOrderRequestHelper = getOrderRequestProcessor;
		this.captureOrderRequestHelper = captureOrderRequestProcessor;
	}

	@Override
	public OrderDTO createOrder(CreateOrderReqDTO createOrderReqDTO) {

		System.out.println("Creating order for "
				+ "createOrderReqDTO: " + createOrderReqDTO);


		HttpRequest httpRequest = createOrderRequestHelper.prepareRequest(
				createOrderReqDTO);

		System.out.println("Sending request to HttpClientUtil httpRequest: " + httpRequest);

		ResponseEntity<String> createOrderResponse = httpClientUtil.makeHttpRequest(httpRequest);

		String createOrderResponseAsJson = createOrderResponse.getBody();
		OrderResponse resAsObj = gson.fromJson(createOrderResponseAsJson, OrderResponse.class);

		System.out.println("Got createOrderResponse:** resAsObj" + resAsObj);

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(resAsObj.getId());
		orderDTO.setStatus(resAsObj.getStatus());

		String redirectUrl = resAsObj.getLinks().stream()
				.filter(link -> Constant.REDIRECT_URL_PAYER_ACTION.equals(link.getRel()))
				.map(Link::getHref)
				.findFirst()
				.orElse(null);

		orderDTO.setRedirectUrl(redirectUrl);

		System.out.println("Returning created orderDTO: " + orderDTO);

		return orderDTO;
	}

	@Override
	public OrderDTO captureOrder(String orderId) {
		HttpRequest httpRequest = captureOrderRequestHelper.prepareRequest(orderId);

		System.out.println("getOrder|| sending request to HttpClientUtil httpRequest: " + httpRequest);
		
		ResponseEntity<String> captureOrderResponse = httpClientUtil.makeHttpRequest(httpRequest);

		String captureOrderResponseAsJson = captureOrderResponse.getBody();
		OrderResponse resAsObj = gson.fromJson(captureOrderResponseAsJson, OrderResponse.class);

		System.out.println("Got createOrderResponse:** resAsObj" + resAsObj);

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(resAsObj.getId());
		orderDTO.setStatus(resAsObj.getStatus());

		String redirectUrl = resAsObj.getLinks().stream()
				.filter(link -> Constant.REDIRECT_URL_PAYER_ACTION.equals(link.getRel()))
				.map(Link::getHref)
				.findFirst()
				.orElse(null);

		orderDTO.setRedirectUrl(redirectUrl);

		System.out.println("Returning created orderDTO: " + orderDTO);

		return orderDTO;
	}

	@Override
	public OrderDTO getOrder(String orderId) {
		HttpRequest httpRequest = getOrderRequestHelper.prepareRequest(orderId);

		System.out.println("getOrder|| sending request to HttpClientUtil httpRequest: " + httpRequest);
		
		ResponseEntity<String> getOrderResponse = httpClientUtil.makeHttpRequest(httpRequest);

		String createOrderResponseAsJson = getOrderResponse.getBody();
		OrderResponse resAsObj = gson.fromJson(createOrderResponseAsJson, OrderResponse.class);

		System.out.println("Got createOrderResponse:** resAsObj" + resAsObj);

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(resAsObj.getId());
		orderDTO.setStatus(resAsObj.getStatus());

		String redirectUrl = resAsObj.getLinks().stream()
				.filter(link -> Constant.REDIRECT_URL_PAYER_ACTION.equals(link.getRel()))
				.map(Link::getHref)
				.findFirst()
				.orElse(null);

		orderDTO.setRedirectUrl(redirectUrl);

		System.out.println("Returning created orderDTO: " + orderDTO);

		return orderDTO;
	}

}
