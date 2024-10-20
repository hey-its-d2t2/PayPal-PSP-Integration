package com.cpt.payments.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {
	
	GENERIC_ERROR("30000", "Unable to process the request, please try later"),
    ERROR_CONNECTING_TO_PAYPAL("30001", "Error Connecting to Paypal, please try later"),
    INVALID_RESPONSE_FROM_PAYPAL("30002", "Invalid response from paypal, please try later");

    private final String errorCode;
    private final String errorMessage;

    // Constructor for the enum
    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

