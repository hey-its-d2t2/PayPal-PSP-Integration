package com.cpt.payments.paypal.res;

import java.util.List;

import com.cpt.payments.paypal.PaymentSource;
import com.cpt.payments.paypal.req.Amount;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
	
	private String id;
    private String status;

    @SerializedName("payment_source")
    private PaymentSource paymentSource;

    private List<Link> links;

}
