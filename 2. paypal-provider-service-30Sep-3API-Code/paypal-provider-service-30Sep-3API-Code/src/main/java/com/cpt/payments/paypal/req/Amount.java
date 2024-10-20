package com.cpt.payments.paypal.req;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amount {

	@SerializedName("currency_code")
    private String currencyCode;
    
    private String value;

}
