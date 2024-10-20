package com.cpt.payments.paypal.res;

import com.cpt.payments.paypal.req.Amount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Link {
	
	private String href;
    private String rel;
    private String method;

}
