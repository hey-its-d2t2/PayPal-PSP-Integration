package com.cpt.payments.service.interfaces;

import com.cpt.payments.dto.TransactionDTO;

public interface ProviderHandler {

	public void reconcileTransaction(TransactionDTO transactionDTO);
}
