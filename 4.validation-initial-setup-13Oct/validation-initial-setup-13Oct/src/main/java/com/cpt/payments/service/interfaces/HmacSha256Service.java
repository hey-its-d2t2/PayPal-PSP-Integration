package com.cpt.payments.service.interfaces;

public interface HmacSha256Service {
	
	public String calculateHMAC(String data);

	public boolean verifyHMAC(String data, String receivedHmac);

}
