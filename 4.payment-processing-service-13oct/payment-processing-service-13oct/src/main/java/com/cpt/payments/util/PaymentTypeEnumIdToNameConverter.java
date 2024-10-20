package com.cpt.payments.util;

import org.modelmapper.AbstractConverter;

import com.cpt.payments.constant.PaymentTypeEnum;

public class PaymentTypeEnumIdToNameConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return PaymentTypeEnum.getById(source).getName();
	}

}
