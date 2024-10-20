package com.cpt.payments.paypalprovider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalProviderErrorResponse {

	private String errorCode;
	private String errorMessage;

}
