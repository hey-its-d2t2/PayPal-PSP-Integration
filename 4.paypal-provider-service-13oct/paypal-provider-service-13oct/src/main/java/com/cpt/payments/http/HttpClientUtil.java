package com.cpt.payments.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClientUtil {

    private final RestTemplate restTemplate;

    public HttpClientUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> makeHttpRequest(
    		HttpRequest httpRequest) {
        try {
        	System.out.println("HttpClientUtil:: Making HTTP Call for httpRequest:" + httpRequest);
            
            HttpEntity<Object> entity = new HttpEntity<>(
            		httpRequest.getRequest(), httpRequest.getHttpHeaders());
            
            ResponseEntity<String> httpResponseObj = restTemplate.exchange(httpRequest.getUrl(), 
            		httpRequest.getMethod(), entity, String.class);
           
            System.out.println("Got HTTP Response httpResponseObj:" + httpResponseObj);
            return httpResponseObj;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
        	ex.printStackTrace();
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
        	ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
