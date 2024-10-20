package com.cpt.payments.service.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.Constant;
import com.cpt.payments.dto.CreateOrderReqDTO;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypal.PayPal;
import com.cpt.payments.paypal.PaymentSource;
import com.cpt.payments.paypal.req.Amount;
import com.cpt.payments.paypal.req.ExperienceContext;
import com.cpt.payments.paypal.req.PaymentRequest;
import com.cpt.payments.paypal.req.PurchaseUnit;
import com.cpt.payments.service.TokenService;
import com.google.gson.Gson;

@Component
public class CreateOrderRequestHelper {
	
	@Value("${paypal.clientid}")
	private String clientId;

	@Value("${paypal.clientsecret}")
	private String clientSecret;

	@Value("${paypal.createOrder.url}")
	private String createOrderUrl;
	
	private Gson gson;

	private TokenService tokenService;
	
	public CreateOrderRequestHelper(TokenService tokenService, Gson gson) {
		this.tokenService = tokenService;
		this.gson = gson;
	}

	public HttpRequest prepareRequest(CreateOrderReqDTO createOrderReqDTO) {
		System.out.println("CreateOrderRequestHelper preparing Request "
				+ "createOrderReqDTO:" + createOrderReqDTO);
		
		HttpRequest httpRequest = new HttpRequest();

		httpRequest.setUrl(createOrderUrl);
		httpRequest.setMethod(HttpMethod.POST);

		HttpHeaders headers = new HttpHeaders();
		
		String accessToken = tokenService.getAccessToken();
		System.out.println("Got access token: " + accessToken);
		
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(Constant.PAY_PAL_REQUEST_ID, createOrderReqDTO.getTxnRef());


		httpRequest.setHttpHeaders(headers);


		Amount amount = Amount.builder()
				.currencyCode(createOrderReqDTO.getCurrencyCode())
				.value(createOrderReqDTO.getAmountValue())
				.build();
		
		List<PurchaseUnit> listPurchaseUnit = List.of(
				PurchaseUnit.builder()
				.amount(amount)
				.build()
				);
		
		PaymentRequest paymentRequest = PaymentRequest.builder()
				.intent(Constant.INTENT_CAPTURE)
				.purchaseUnits(listPurchaseUnit)
				.paymentSource(PaymentSource.builder()
						.paypal(PayPal.builder()
								.experienceContext(ExperienceContext.builder()
										.shippingPreference(Constant.SHIPPING_GET_FROM_FILE)
										.landingPage(Constant.LANDING_PAGE_LOGIN)
										.userAction(Constant.USER_ACTION_PAY_NOW)
										.paymentMethodPreference(Constant.PAYMENT_METHOD_IMMEDIATE_PAYMENT_REQUIRED)
										
										.brandName(createOrderReqDTO.getBrandName())
										.locale(createOrderReqDTO.getLocale())
										.returnUrl(createOrderReqDTO.getReturnUrl())
										.cancelUrl(createOrderReqDTO.getCancelUrl())
										.build())
								.build())
						.build())
				.build();

		String reqAsJson = gson.toJson(paymentRequest);
		
		httpRequest.setRequest(reqAsJson);
		
		System.out.println("CreateOrderRequestHelper:: Prepared Request:" + httpRequest);
		
		return httpRequest;
	}

}
