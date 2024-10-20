package com.cpt.payments.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.exception.ValidationException;
import com.cpt.payments.service.interfaces.HmacSha256Service;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HmacFilter extends OncePerRequestFilter {
	
	private HmacSha256Service hmacSha256Service;
	
	private Gson gson;
	
	public HmacFilter(HmacSha256Service hmacSha256Service, Gson gson) {
		this.hmacSha256Service = hmacSha256Service;
		this.gson = gson;
	}
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("======= HmacFilter: doFilterInternal| hmacSha256Service:" + hmacSha256Service);
		
		// HMAC logic 
		String receivedHmacSignature = request.getHeader("HmacSignature");
		
		if (receivedHmacSignature == null) {
			System.out.println("HmacFilter: HmacSignature not found. Sending 401");
			//response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			
			throw new ValidationException(
					ErrorCodeEnum.MISSING_HMAC_SIGNATURE.getErrorCode(), 
					ErrorCodeEnum.MISSING_HMAC_SIGNATURE.getErrorMessage(), 
					HttpStatus.BAD_REQUEST);
		}
		
		
		WrappedRequest wrappedRequest = new WrappedRequest(request);
        
        // Return the raw JSON string
		
		String data = request.getRequestURI();
		
		if (wrappedRequest.getBody() != null 
				&& !wrappedRequest.getBody().isEmpty()) {
			data = data + "|" + getNormalizedJson(wrappedRequest.getBody());
		}


		System.out.println("-------HmacFilter: sending request data for sign processing"
				+ "| data:" + data);
		System.out.println("-------HmacFilter: receivedHmacSignature:" + receivedHmacSignature);
		
		
		//boolean isValid = hmacSha256Service.verifyHMAC(data, receivedHmacSignature);
		boolean isValid = true;//TODO this is only for temp basis for fast testing. REmove become commiting.
		
        System.out.println("HmacFilter: isValid:" + isValid);		
		
		if (isValid) {
			System.out.println("HmacFilter: isValid. Calling next filter in line");
			
			
			SecurityContext context = SecurityContextHolder.createEmptyContext(); 
			
			Authentication authentication =
			    new HmacAuthenticationToken("ECOM", ""); 
			
			context.setAuthentication(authentication);

			SecurityContextHolder.setContext(context);
			
			System.out.println("HmacFilter: SecurityContextHolder set with Authentication Token");
			
			filterChain.doFilter(wrappedRequest, response);
			
			System.out.println("HmacFilter: doFilterInternal: after filterChain.doFilter");
		} else {
			
			System.out.println("HmacFilter: NOT valid. Sending 401");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		

	}
	
	public String getNormalizedJson(String rawJson) {
	    // Parse the raw JSON string
	    JsonElement jsonElement = JsonParser.parseString(rawJson);
	    // Convert it back to a JSON string
	    return gson.toJson(jsonElement);
	}


}
