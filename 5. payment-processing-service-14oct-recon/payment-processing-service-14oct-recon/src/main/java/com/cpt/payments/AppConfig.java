package com.cpt.payments;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.entity.TransactionEntity;
import com.cpt.payments.util.PaymentMethodEnumIdToNameConverter;
import com.cpt.payments.util.PaymentTypeEnumIdToNameConverter;
import com.cpt.payments.util.ProviderEnumIdToNameConverter;
import com.cpt.payments.util.TransactionStatusEnumIdToNameConverter;

@Configuration
public class AppConfig {

	@Bean
    ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-Task-");
        executor.initialize();
        return executor;
    }
	
	@Bean
	ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

		
        Converter<Integer, String> paymentMethodEnumIdToNameConverter = 
        		new PaymentMethodEnumIdToNameConverter();
        Converter<Integer, String> paymentTypeEnumIdToNameConverter = 
        		new PaymentTypeEnumIdToNameConverter();
        Converter<Integer, String> providerEnumIdToNameConverter = 
        		new ProviderEnumIdToNameConverter();
        
        Converter<Integer, String> transactionStatusEnumIdToNameConverter = 
        		new TransactionStatusEnumIdToNameConverter();
        
        
        modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDTO>() {
            @Override
            protected void configure() {
                using(paymentMethodEnumIdToNameConverter).map(
                		source.getPaymentMethodId(), destination.getPaymentMethod());
                
                using(paymentTypeEnumIdToNameConverter).map(
                		source.getPaymentTypeId(), destination.getPaymentType());
                
                using(providerEnumIdToNameConverter).map(
                		source.getProviderId(), destination.getProvider());
                
                using(transactionStatusEnumIdToNameConverter).map(
                		source.getTxnStatusId(), destination.getTxnStatus());
                
            }
        });
		
		return modelMapper;
	}
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
