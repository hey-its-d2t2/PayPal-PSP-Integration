package com.cpt.payments.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpt.payments.pojo.CreatePaymentRes;
import com.cpt.payments.pojo.PaymentRequest;
import com.cpt.payments.service.interfaces.HmacSha256Service;
import com.cpt.payments.service.interfaces.PaymentService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

	private HmacSha256Service hmacSha256Service;

	private Gson gson;

	private PaymentService paymentService;
	
	
	@Value("${mytestkey}")
	private String myTestKey;
	

	public PaymentController(HmacSha256Service hmacSha256Service, Gson gson, 
			PaymentService paymentService) {
		this.hmacSha256Service = hmacSha256Service;
		this.gson = gson;
		this.paymentService = paymentService;
	}

	/**
	 * 1. receive pojo
	 * 2. Use ModelMapper to convert to dto
	 * 3. Make service call by passing dto
	 * 4. Convert response dto to pojo
	 * 5. return response
	 *  
	 * @param paymentRequest
	 * @return
	 */
	@PostMapping
	public ResponseEntity<CreatePaymentRes> createPayment(
			@RequestBody PaymentRequest paymentRequest) {

		System.out.println("createpayment request received "
				+ "| paymentRequest:" + paymentRequest);

		CreatePaymentRes createPaymentRes = paymentService.createPayment(paymentRequest);			

		System.out.println("createPayment received from controller response:" + createPaymentRes);

		/*
		 * ResponseEntity<CreatePaymentRes> responseEntity =
		 * ResponseEntity.ok(createPaymentRes);
		 */

		ResponseEntity<CreatePaymentRes> responseEntity = new ResponseEntity<CreatePaymentRes>(
				createPaymentRes, HttpStatus.CREATED);

		System.out.println("Controller returning responseEntity:" + responseEntity);

		return responseEntity;
	}


	@PostMapping("/{txnRef}/capture")
	public String capturePayment(@PathVariable String txnRef) {
		System.out.println("capturePayment request received | txnRef:" + txnRef);

		// TODO handle hmac logic

		return "Payment captured:" + txnRef;
	}

	@GetMapping("/{txnRef}")
	public String getPayment(@PathVariable String txnRef) {
		System.out.println("getPayment request received | txnRef:" + txnRef);

		System.out.println("myTestKey:" + myTestKey);
		
		// TODO handle hmac logic

		return "getPayment:" + txnRef + "| myTestKey : " + myTestKey;
	}

}
