package com.cpt.payments.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.pojo.ErrorResponse;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
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
