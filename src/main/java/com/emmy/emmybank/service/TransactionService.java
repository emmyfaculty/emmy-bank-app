package com.emmy.emmybank.service;

import com.emmy.emmybank.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
	void saveTransaction(TransactionDto transaction);

}
