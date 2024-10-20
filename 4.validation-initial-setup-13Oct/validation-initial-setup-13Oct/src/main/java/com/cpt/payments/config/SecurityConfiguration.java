package com.cpt.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;

import com.cpt.payments.exception.CustomAccessDeniedHandler;
import com.cpt.payments.exception.CustomAuthenticationEntryPoint;
import com.cpt.payments.exception.ExceptionHandlerFilter;
import com.cpt.payments.security.HmacFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfiguration {

	private HmacFilter hmacFilter;

	private ExceptionHandlerFilter exceptionHandlerFilter;

	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	private CustomAccessDeniedHandler customAccessDeniedHandler;

	public SecurityConfiguration(HmacFilter hmacFilter, 
			ExceptionHandlerFilter exceptionHandlerFilter, 
			CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
			CustomAccessDeniedHandler customAccessDeniedHandler) {
		this.hmacFilter = hmacFilter;
		this.exceptionHandlerFilter = exceptionHandlerFilter;
		this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http
		.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint(customAuthenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler)
				)
		.csrf(csrf -> csrf.disable())

		.authorizeHttpRequests(authorize -> authorize
				.anyRequest().authenticated()
				)

		.addFilterBefore(exceptionHandlerFilter, DisableEncodeUrlFilter.class)
		.addFilterBefore(hmacFilter, AuthorizationFilter.class)

		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}

