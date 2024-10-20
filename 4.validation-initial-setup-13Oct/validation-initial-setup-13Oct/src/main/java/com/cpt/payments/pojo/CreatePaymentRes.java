package com.cpt.payments.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRes {
	
	private String txnRef;
	private String providerRef;
	private String redirectUrl;

}
