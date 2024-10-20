package com.cpt.payments.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpt.payments.constant.Constants;
import com.cpt.payments.constant.PaypalStatus;
import com.cpt.payments.constant.TransactionStatusEnum;
import com.cpt.payments.dao.interfaces.TransactionDAO;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.http.HttpClientUtil;
import com.cpt.payments.http.HttpRequest;
import com.cpt.payments.paypalprovider.Order;
import com.cpt.payments.service.helper.PaypalProviderCaptureOrderRequestHelper;
import com.cpt.payments.service.helper.PaypalProviderCaptureOrderResponseHelper;
import com.cpt.payments.service.helper.PaypalProviderGetOrderRequestHelper;
import com.cpt.payments.service.helper.PaypalProviderGetOrderResponseHelper;
import com.cpt.payments.service.interfaces.ProviderHandler;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaypalProviderHandler implements ProviderHandler {

	private HttpClientUtil httpClientUtil;

	private PaypalProviderGetOrderRequestHelper paypalProviderGetOrderRequestHelper;

	private PaypalProviderGetOrderResponseHelper paypalProviderGetOrderResponseHelper;
	
	private PaypalProviderCaptureOrderRequestHelper paypalProviderCaptureOrderRequestHelper;

	private PaypalProviderCaptureOrderResponseHelper paypalProviderCaptureOrderResponseHelper;

	private Gson gson;
	
	private TransactionDAO transactionDAO;

	public PaypalProviderHandler(HttpClientUtil httpClientUtil, Gson gson, 
			PaypalProviderGetOrderRequestHelper paypalProviderGetOrderRequestHelper,
			PaypalProviderGetOrderResponseHelper paypalProviderGetOrderResponseHelper,
			TransactionDAO transactionDAO,
			PaypalProviderCaptureOrderRequestHelper paypalProviderCaptureOrderRequestHelper,
			PaypalProviderCaptureOrderResponseHelper paypalProviderCaptureOrderResponse) {
		this.httpClientUtil = httpClientUtil;
		this.gson = gson;
		this.paypalProviderGetOrderRequestHelper = paypalProviderGetOrderRequestHelper;
		this.paypalProviderGetOrderResponseHelper = paypalProviderGetOrderResponseHelper;
		this.transactionDAO = transactionDAO;
		this.paypalProviderCaptureOrderRequestHelper = paypalProviderCaptureOrderRequestHelper;
		this.paypalProviderCaptureOrderResponseHelper = paypalProviderCaptureOrderResponse;
	}

	@Override
	public void reconcileTransaction(TransactionDTO transactionDTO) {
		log.info("reconcileTransaction() called. transactionDTO:" + transactionDTO);

		transactionDTO.setRetryCount(transactionDTO.getRetryCount() + 1);// increment retry count
		String initialStatus = transactionDTO.getTxnStatus(); // initial status

		try {
			Order orderObj = getPaypalOrderStatus(transactionDTO);
			log.info("orderObj: " + orderObj);

			PaypalStatus status = PaypalStatus.getEnumFromString(orderObj.getStatus());

			switch (status) {
			case PAYER_ACTION_REQUIRED:
				// no action
				
				break;
			case APPROVED:
				// call capture order
				// success / fail
				try {
					Order captureResponse = capturePaypalOrder(transactionDTO);
					System.out.println("captureResponse: " + captureResponse);
					
					transactionDTO.setTxnStatus(TransactionStatusEnum.SUCCESS.name());
					
				} catch (Exception e) {
					// still retry happened.
					// manual review
					//TODO
				}
				break;

			case COMPLETED:
				// update transactionDTO with success
				transactionDTO.setTxnStatus(TransactionStatusEnum.SUCCESS.name());
				break;
			default:
				// manual review
				break;
			}
		} catch (Exception e) {
			// still retry happened.
			log.error("Error while reconciling transaction. transactionDTO:" + transactionDTO, e);
			log.info("continue with recon activity");
		}

		//update transactionDTO to DB
		
		String finalStatus = transactionDTO.getTxnStatus();

		// PENDING changed to SUCCESS, or APPROVED changed to SUCCESS
		if (!initialStatus.equals(finalStatus)) {
			System.out.println("Txn updated, can return"); 
			boolean isUpdated = transactionDAO.updateTransactionReconDetails(transactionDTO);
			log.info("Updated ReconTxn in DB. isUpdated:" + isUpdated);
			return;
		}

		if(transactionDTO.getRetryCount() >= Constants.PAYPAL_RECON_MAX_RETRY) {
			// Has reached max retry count
			// update transactionDTO with fail
			transactionDTO.setTxnStatus(TransactionStatusEnum.FAILED.name());
		}
		
		boolean isUpdated = transactionDAO.updateTransactionReconDetails(transactionDTO);
		log.info("Updated ReconTxn in DB. isUpdated:" + isUpdated);
	}

	private Order getPaypalOrderStatus(TransactionDTO transactionDTO) {
		log.info("getPaypalOrderStatus() called. transactionDTO:" + transactionDTO);

		HttpRequest httpRequest = paypalProviderGetOrderRequestHelper.prepareRequest(
				transactionDTO.getProviderReference());

		ResponseEntity<String> getOrderResponse = httpClientUtil.makeHttpRequest(httpRequest);

		Order orderObj = paypalProviderGetOrderResponseHelper.processResponse(getOrderResponse);

		log.info("Got orderObj: " + orderObj);
		return orderObj;
	}
	
	private Order capturePaypalOrder(TransactionDTO transactionDTO) {
		log.info("getPaypalOrderStatus() called. transactionDTO:" + transactionDTO);

		HttpRequest httpRequest = paypalProviderCaptureOrderRequestHelper.prepareRequest(
				transactionDTO.getProviderReference());

		ResponseEntity<String> captureOrderResponse = httpClientUtil.makeHttpRequest(httpRequest);

		Order orderObj = paypalProviderCaptureOrderResponseHelper.processResponse(captureOrderResponse);

		log.info("Got orderObj: " + orderObj);
		return orderObj;
	}

}
