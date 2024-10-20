package com.cpt.payments.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpt.payments.constant.Constant;
import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.dto.CreateOrderReqDTO;
import com.cpt.payments.dto.OrderDTO;
import com.cpt.payments.exception.PaypalProviderException;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypal.PaypalErrorResponse;
import com.cpt.payments.paypal.res.Link;
import com.cpt.payments.paypal.res.OrderResponse;
import com.cpt.payments.service.helper.CaptureOrderRequestHelper;
import com.cpt.payments.service.helper.CreateOrderRequestHelper;
import com.cpt.payments.service.helper.GetOrderRequestHelper;
import com.cpt.payments.service.interfaces.PaymentService;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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


		if(getOrderResponse == null 
				|| getOrderResponse.getBody() == null
				|| getOrderResponse.getBody().trim().isEmpty()) {
			
			log.error("Error connecting to Paypal provider. " + "getOrderResponse is null or empty. "
					+ "getOrderResponse:" + getOrderResponse);

			throw new PaypalProviderException(
					ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorCode(), 
					ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// got data, success / failed
		
		String responseBody = getOrderResponse.getBody();

		if (getOrderResponse.getStatusCode() != HttpStatus.OK) {
			// failed
			// throw custom exception to share this situation.

			if (getOrderResponse.getStatusCode().is4xxClientError() 
					|| getOrderResponse.getStatusCode().is5xxServerError()) {
				// Order not found.
				// throw custom exception to share this situation.

				// json error to come from Paypal.
				
				PaypalErrorResponse resAsObj = gson.fromJson(responseBody, PaypalErrorResponse.class);
				
				log.error("Got errores response from Paypal| resAsObj: " + resAsObj);
				
				String errorCode;
				String errorMessage;
				if(resAsObj.getError() != null) {
					errorCode = resAsObj.getError(); //identity
					errorMessage = resAsObj.getError_description();
				} else {
					errorCode = resAsObj.getName(); // functiona
					errorMessage = resAsObj.getMessage();
				}
				
				throw new PaypalProviderException(
						errorCode, errorMessage, 
						HttpStatus.valueOf(getOrderResponse.getStatusCode().value()));
			}

			// we got non 200, but its not 4xx or 5xx.. 
			//this also is error, but we dont know which json error

			throw new PaypalProviderException(
					ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorCode(), 
					ErrorCodeEnum.INVALID_RESPONSE_FROM_PAYPAL.getErrorMessage(), 
					HttpStatus.BAD_GATEWAY);
		}

		// success - only 200.
		OrderResponse resAsObj = gson.fromJson(responseBody, OrderResponse.class);

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
