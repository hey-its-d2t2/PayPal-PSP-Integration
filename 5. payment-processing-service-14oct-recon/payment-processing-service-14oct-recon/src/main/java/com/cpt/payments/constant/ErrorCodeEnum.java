package com.cpt.payments.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {
	
	GENERIC_ERROR("20000", "Unable to process the request, please try later"),
    ERROR_CONNECTING_PAYPALPROVIDER("20001", "Unable to connect to PaypalProvider");

    private final String errorCode;
    private final String errorMessage;

    // Constructor for the enum
    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

