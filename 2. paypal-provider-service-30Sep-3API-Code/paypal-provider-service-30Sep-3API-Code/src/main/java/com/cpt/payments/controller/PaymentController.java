package com.cpt.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpt.payments.dto.CreateOrderReqDTO;
import com.cpt.payments.dto.OrderDTO;
import com.cpt.payments.pojo.Order;
import com.cpt.payments.pojo.PaypalProviderCreateOrderReq;
import com.cpt.payments.service.interfaces.PaymentService;

/**
 * Payment controller
 * 
 * This controller is responsible for handling payment related requests
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/paypal/order")
public class PaymentController {
	
	private ModelMapper modelMapper;
	
	private PaymentService paymentService;
	
	public PaymentController(ModelMapper modelMapper, 
			PaymentService paymentService) {
		this.modelMapper = modelMapper;
		this.paymentService = paymentService;
	}
	
	/**
	 * Create order
	 * 
	 * This method is responsible for creating order
	 * 
	 * @param createOrder
	 * @return Order
	 */
	@PostMapping
	public Order createOrder(
			@RequestBody PaypalProviderCreateOrderReq createOrder) {
		System.out.println("Order created request received createOrder:" + createOrder);
		
		CreateOrderReqDTO reqDTO = modelMapper.map(createOrder, CreateOrderReqDTO.class);
		System.out.println("Converted req pojo to DTO reqDTO:" + reqDTO);
		
		OrderDTO responseDTO = paymentService.createOrder(reqDTO);
		System.out.println("Recived response from Service responseDTO:" + responseDTO);
		
		Order order = modelMapper.map(responseDTO, Order.class);
		System.out.println("Converted service response to POJO & returning order:" + order);
		
		return order;
	}
	
	@PostMapping("/{orderId}/capture")
	public Order captureOrder(@PathVariable String orderId) {
		System.out.println("captureOrder received for orderId:" + orderId);
		
		OrderDTO responseDTO = paymentService.captureOrder(orderId);
		System.out.println("Recived response from Service responseDTO:" + responseDTO);
		
		Order order = modelMapper.map(responseDTO, Order.class);
		System.out.println("Converted service response to POJO & returning order:" + order);
		
		return order;
	}
	
	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable String orderId) {
		System.out.println("getOrder request received for orderId:" + orderId);
		
		OrderDTO responseDTO = paymentService.getOrder(orderId);
		System.out.println("Recived response from Service responseDTO:" + responseDTO);
		
		Order order = modelMapper.map(responseDTO, Order.class);
		System.out.println("Converted service response to POJO & returning order:" + order);
		
		return order;
	}
	

}
