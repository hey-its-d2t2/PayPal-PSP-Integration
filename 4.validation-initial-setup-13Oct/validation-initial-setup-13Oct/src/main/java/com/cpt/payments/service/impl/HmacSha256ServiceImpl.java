package com.cpt.payments.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.cpt.payments.service.interfaces.HmacSha256Service;

@Service
public class HmacSha256ServiceImpl implements HmacSha256Service {

	private static final String HMAC_SHA256 = "HmacSHA256";

	@Override
	public String calculateHMAC(String jsonInput) {
		
		String secretKey = "THIS_IS_MY_SECRET";

        try {
            // Create a SecretKeySpec object from the secret key
            SecretKeySpec keySpec = new SecretKeySpec(
            		secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);

            // Initialize the HMAC-SHA256 Mac instance
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(keySpec);

            // Compute the HMAC-SHA256 signature
            byte[] signatureBytes = mac.doFinal(jsonInput.getBytes(StandardCharsets.UTF_8));

            // Encode the signature in Base64
            String signature = Base64.getEncoder().encodeToString(signatureBytes);

            System.out.println("HMAC-SHA256 Signature: " + signature);
            
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

        System.out.println("HMAC-SHA256 Failed to generated Signature null");
		return null;
	}

	@Override
	public boolean verifyHMAC(String data, String receivedHmac) {
		
		String generatedSignature = calculateHMAC(data);
		
		System.out.println("receivedHmac:" + receivedHmac);
		System.out.println("generatedSignature:" + generatedSignature);
		
		if (generatedSignature != null 
				&& generatedSignature.equals(receivedHmac)) {
			System.out.println("HMAC-SHA256 Signature is valid");
			return true;
		}
		
		System.out.println("HMAC-SHA256 Signature is invalid");
		return false;
	}

}
