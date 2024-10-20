package com.cpt.payments.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.pojo.ErrorResponse;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        
    	ErrorResponse paymentResponse = new ErrorResponse(
				ErrorCodeEnum.GENERIC_ERROR.getErrorCode(), 
				ErrorCodeEnum.GENERIC_ERROR.getErrorMessage()); 
				
		System.out.println(" paymentResponse is -> " + paymentResponse);
		Gson gson = new Gson();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setContentType("application/json");
		response.getWriter().write(gson.toJson(paymentResponse));
		response.getWriter().flush();
    	
    }
}
