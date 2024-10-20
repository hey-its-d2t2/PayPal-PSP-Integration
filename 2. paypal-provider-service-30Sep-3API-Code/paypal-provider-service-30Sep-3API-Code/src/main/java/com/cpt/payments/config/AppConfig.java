package com.cpt.payments.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	/**
	 * Model mapper
	 * 
	 * This method is responsible for creating model mapper
	 * 
	 * @return ModelMapper
	 */
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
