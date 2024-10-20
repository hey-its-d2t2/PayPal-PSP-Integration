package com.cpt.payments.service.recon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cpt.payments.dao.interfaces.TransactionDAO;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.service.interfaces.ReconService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReconServiceImpl implements ReconService{

	private ReconPaymentAsync asyncTaskService;
	
	private TransactionDAO transactionDAO;
	
	public ReconServiceImpl(ReconPaymentAsync asyncTaskService, 
			TransactionDAO transactionDAO) {
		this.asyncTaskService = asyncTaskService;
		this.transactionDAO = transactionDAO;
	}
	
	@Override
	//@Scheduled(cron = "0/15 * * * * ?")
	public void reconcilePayments() {
        log.info("Reconciling payments...");
        
        // 1. Make DB call to load all PENDING payment/transactions from DB.
        // 2. You would get list of all applicable payments.
        
        
        List<TransactionDTO> reconTxns = transactionDAO.getTransactionsForRecon();
        
        log.info("Total txn for recon reconTxns size: " + reconTxns.size());
        
        
		reconTxns.forEach(txn -> {
			log.info("Processing txn: " + txn.getId());

			// process txn
			asyncTaskService.reconAsyn(txn);

		});

        
        //3. For each payment (iterate through all payments),
        	// process each payment Asyncronously. It shoudl happen via different thread.
        
        
        
           // continue with next process
        
        
        //  
        //      call processing shoudl call paypal-provider service for getstatus call.
        // 4. Whatever is the status received, accordingly take actions.
	}

}
