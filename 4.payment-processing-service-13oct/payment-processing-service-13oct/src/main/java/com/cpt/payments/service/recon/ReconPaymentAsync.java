package com.cpt.payments.service.recon;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.ProviderEnum;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.service.impl.PaypalProviderHandler;
import com.cpt.payments.service.interfaces.ProviderHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReconPaymentAsync {

	private ApplicationContext context;
	
	public ReconPaymentAsync(ApplicationContext context) {
		this.context = context;
	}
	
	@Async
	public void reconAsyn(TransactionDTO txn) {
		log.info("reconAsyn() called. txn:" + txn);
		
		ProviderHandler providerHandler = null;
		
		if(txn.getProvider().equals(ProviderEnum.PAYPAL.getName()) ) {
			providerHandler = context.getBean(PaypalProviderHandler.class);
		}
		
		log.info("providerHandler: " + providerHandler);
		if (providerHandler == null) {
			log.error("providerHandler is null. txn:" + txn);
			return;
		}
		
		providerHandler.reconcileTransaction(txn);
		
	}
}
