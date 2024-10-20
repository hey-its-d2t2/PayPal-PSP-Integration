package com.cpt.payments.service.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.exception.ValidationException;
import com.cpt.payments.pojo.CreatePaymentRes;
import com.cpt.payments.pojo.PaymentRequest;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public CreatePaymentRes createPayment(PaymentRequest paymentRequest) {
		
		
		/*
		 * String temp = null; temp.length();
		 */
		
		if (!paymentRequest.getPayment().getProviderId().equals("PAYPAL")) {
			// error, throw Exception
			
			System.out.println("Provider is invalid");
			
			throw new ValidationException(
					ErrorCodeEnum.INVALID_PROVIDER.getErrorCode(), 
					ErrorCodeEnum.INVALID_PROVIDER.getErrorMessage(), 
					HttpStatus.BAD_REQUEST);
		}
		
		System.out.println("Valid request with PAYPAL");
		
		
		
		CreatePaymentRes createPaymentRes = new CreatePaymentRes();
		createPaymentRes.setTxnRef("TxnRef001");
		createPaymentRes.setProviderRef("ProviderRef001");
		createPaymentRes.setRedirectUrl("http://redirecturl.com");
		
		System.out.println("createPayment returning response from service "
				+ "response:" + createPaymentRes);
		
		return createPaymentRes;
	}

}
