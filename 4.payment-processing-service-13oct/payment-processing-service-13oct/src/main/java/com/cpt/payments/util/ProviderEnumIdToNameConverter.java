package com.cpt.payments.util;

import org.modelmapper.AbstractConverter;

import com.cpt.payments.constant.ProviderEnum;

public class ProviderEnumIdToNameConverter extends AbstractConverter<Integer, String> {

	@Override
	protected String convert(Integer source) {
		return ProviderEnum.getById(source).getName();
	}

}
