package com.cpt.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequest {

	private String url;
	private HttpMethod method; 
	private Object request;
	private HttpHeaders httpHeaders;

}
