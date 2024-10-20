package com.cpt.payments.paypal;

import com.cpt.payments.paypal.req.ExperienceContext;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayPal {
	
	@SerializedName("experience_context")
    private ExperienceContext experienceContext;

}
