package com.cpt.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.cpt.payments.security.HmacFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfiguration {
	
	private HmacFilter hmacFilter;
	
	public SecurityConfiguration(HmacFilter hmacFilter) {
		this.hmacFilter = hmacFilter;
	}
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		http
		.csrf(csrf -> csrf.disable())

		.authorizeHttpRequests(authorize -> authorize
				.anyRequest().authenticated()
				)

		.addFilterBefore(hmacFilter, AuthorizationFilter.class)
		
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}

