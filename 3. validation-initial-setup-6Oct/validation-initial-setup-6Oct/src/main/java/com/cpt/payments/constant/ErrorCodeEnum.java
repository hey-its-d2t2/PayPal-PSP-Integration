package com.cpt.payments.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {
    INVALID_PROVIDER(10001, "Invalid providerId, currently we process only PAYPAL"),
    INVALID_PAYMENT_METHOD(10002, "Invalid paymentMethod, currently only APMs are supported");

    private final int errorCode;
    private final String errorMessage;

    // Constructor for the enum
    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

