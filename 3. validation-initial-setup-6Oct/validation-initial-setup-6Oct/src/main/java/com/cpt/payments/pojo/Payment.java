package com.cpt.payments.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

	private String currency;
    private String amount;
    private String brandName;
    private String locale;
    private String returnUrl;
    private String cancelUrl;
    private String country;
    private String merchantTxnRef;
    private String paymentMethod;
    private String providerId;
    private String paymentType;
}
