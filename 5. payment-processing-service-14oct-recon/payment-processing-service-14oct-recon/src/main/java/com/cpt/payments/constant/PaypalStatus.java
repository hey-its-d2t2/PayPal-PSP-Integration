package com.cpt.payments.constant;

import lombok.ToString;

@ToString
public enum PaypalStatus {
	
	PAYER_ACTION_REQUIRED("PAYER_ACTION_REQUIRED"),
	APPROVED("APPROVED"),
	COMPLETED("COMPLETED");
	

	private final String name;

	PaypalStatus(String name) {
		this.name = name;
	}
	
	// method to return Enum, if we pass the name
	public static PaypalStatus getEnumFromString(String name) {
		for (PaypalStatus status : PaypalStatus.values()) {
			if (status.name.equalsIgnoreCase(name)) {
				return status;
			}
		}
		return null;
	}
}
