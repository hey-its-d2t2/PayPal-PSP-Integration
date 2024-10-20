package com.cpt.payments.service.interfaces;

import com.cpt.payments.dto.CreateOrderReqDTO;
import com.cpt.payments.dto.OrderDTO;

public interface PaymentService {
	
	public OrderDTO createOrder(CreateOrderReqDTO createOrderReqDTO);

	public OrderDTO captureOrder(String orderId);

	public OrderDTO getOrder(String orderId);

}
