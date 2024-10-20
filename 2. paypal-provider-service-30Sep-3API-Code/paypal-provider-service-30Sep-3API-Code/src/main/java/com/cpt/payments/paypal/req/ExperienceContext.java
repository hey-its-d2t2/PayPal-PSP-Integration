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
public class ExperienceContext {
	
	@SerializedName("brand_name")
    private String brandName;
    
    @SerializedName("shipping_preference")
    private String shippingPreference;
    
    @SerializedName("landing_page")
    private String landingPage;
    
    @SerializedName("user_action")
    private String userAction;
    
    private String locale;
    
    @SerializedName("payment_method_preference")
    private String paymentMethodPreference;
    
    @SerializedName("return_url")
    private String returnUrl;
    
    @SerializedName("cancel_url")
    private String cancelUrl;

}
