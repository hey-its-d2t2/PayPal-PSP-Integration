package com.cpt.payments.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {
	
	GENERIC_ERROR(10000, "Unable to process the request, please try later"),
    INVALID_PROVIDER(10001, "Invalid providerId, currently we process only PAYPAL"),
    INVALID_PAYMENT_METHOD(10002, "Invalid paymentMethod, currently only APMs are supported"),
    MISSING_HMAC_SIGNATURE(10003, "HmacSignature is missing in the request header");

    private final int errorCode;
    private final String errorMessage;

    // Constructor for the enum
    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

