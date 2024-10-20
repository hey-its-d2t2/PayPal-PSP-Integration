package com.cpt.payments.service.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.exception.ProcessingException;
import com.cpt.payments.paypalprovider.Order;
import com.cpt.payments.paypalprovider.PaypalProviderErrorResponse;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaypalProviderGetOrderResponseHelper {

	private Gson gson;

	public PaypalProviderGetOrderResponseHelper(Gson gson) {
		this.gson = gson;
	}


	public Order processResponse(ResponseEntity<String> getOrderResponse) {
		if(getOrderResponse == null 
				|| getOrderResponse.getBody() == null
				|| getOrderResponse.getBody().trim().isEmpty()) {

			log.error("Error connecting to Paypal provider. " + "getOrderResponse is null or empty. "
					+ "getOrderResponse:" + getOrderResponse);

			throw new ProcessingException(
					ErrorCodeEnum.ERROR_CONNECTING_PAYPALPROVIDER.getErrorCode(), 
					ErrorCodeEnum.ERROR_CONNECTING_PAYPALPROVIDER.getErrorMessage(), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// got data, success / failed

		String responseBody = getOrderResponse.getBody();

		if (getOrderResponse.getStatusCode() != HttpStatus.OK) {
			// failed

			PaypalProviderErrorResponse resAsObj = gson.fromJson(responseBody, PaypalProviderErrorResponse.class);

			log.error("Got errores response from Paypal| resAsObj: " + resAsObj);

			throw new ProcessingException(
					resAsObj.getErrorCode(), resAsObj.getErrorMessage(), 
					HttpStatus.valueOf(getOrderResponse.getStatusCode().value()));
		}

		// success
		Order orderObj = gson.fromJson(responseBody, Order.class);
		log.info("Got success Order ref from Paypal orderObj: " + orderObj);
		return orderObj;
	}
}
