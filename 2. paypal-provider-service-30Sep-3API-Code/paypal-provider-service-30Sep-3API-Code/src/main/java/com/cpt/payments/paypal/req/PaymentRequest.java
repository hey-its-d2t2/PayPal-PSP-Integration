package com.cpt.payments.paypal.req;

import java.util.List;

import com.cpt.payments.paypal.PaymentSource;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

	private String intent;

	@SerializedName("purchase_units")
	private List<PurchaseUnit> purchaseUnits;

	@SerializedName("payment_source")
	private PaymentSource paymentSource;


}
