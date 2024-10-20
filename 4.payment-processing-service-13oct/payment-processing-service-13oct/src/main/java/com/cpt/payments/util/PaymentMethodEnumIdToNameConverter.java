package com.cpt.payments.util;

import org.modelmapper.AbstractConverter;

import com.cpt.payments.constant.PaymentMethodEnum;

public class PaymentMethodEnumIdToNameConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return PaymentMethodEnum.getById(source).getName();
	}

}
