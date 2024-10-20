package com.cpt.payments.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cpt.payments.constant.TransactionStatusEnum;
import com.cpt.payments.dao.interfaces.TransactionDAO;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.entity.TransactionEntity;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TransactionDAOImpl implements TransactionDAO {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private ModelMapper modelMapper;
	
	public TransactionDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, 
			ModelMapper modelMapper) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<TransactionDTO> getTransactionsForRecon() {
		String sql = "SELECT * FROM Transaction "
				+ "WHERE txnStatusId IN ( :txnStatusId1, :txnStatusId2 ) AND retryCount < :retryCount";

        Map<String, Object> params = new HashMap<>();
        params.put("txnStatusId1", 3); // PENDING status
        params.put("txnStatusId2", 4); // APPROVED status
        
        params.put("retryCount", 3);  // retryCount < 3

        List<TransactionEntity> reconTxnEntities = namedParameterJdbcTemplate.query(
                sql,
                params,
                new BeanPropertyRowMapper<>(TransactionEntity.class)
        );
        
        List<TransactionDTO> reconTxnDTOs = convertToDto(reconTxnEntities);
        
        log.info("Total txn for recon reconTxns size: " + reconTxnEntities.size());
        
		return reconTxnDTOs;
	}
	
	private List<TransactionDTO> convertToDto(List<TransactionEntity> transactionEntities) {
        return transactionEntities.stream()
                .map(entity -> modelMapper.map(entity, TransactionDTO.class))
                .collect(Collectors.toList());
    }

	@Override
	public boolean updateTransactionReconDetails(TransactionDTO transactionDTO) {
		String sql = "UPDATE Transaction "
				+ "SET retryCount = :retryCount, txnStatusId = :txnStatusId "
				+ "WHERE id = :id";

        // Map the parameters
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("retryCount", transactionDTO.getRetryCount());
        params.addValue("txnStatusId", 
        		TransactionStatusEnum.getByName(transactionDTO.getTxnStatus()).getId());
        params.addValue("id", transactionDTO.getId());

        // Execute the update query
        int updateCount = namedParameterJdbcTemplate.update(sql, params);
		
        System.out.println("updateCount: " + updateCount);
        
		return updateCount > 0;
	}

}
