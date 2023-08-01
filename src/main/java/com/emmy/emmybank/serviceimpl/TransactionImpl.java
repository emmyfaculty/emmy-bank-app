package com.emmy.emmybank.serviceimpl;

import com.emmy.emmybank.constants.Status;
import com.emmy.emmybank.dto.TransactionDto;
import com.emmy.emmybank.entity.Transaction;
import com.emmy.emmybank.repository.TransactionRepository;
import com.emmy.emmybank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionImpl implements TransactionService {
	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.transactionAmount(transactionDto.getTransactionAmount())
				.transactionAccountNumber(transactionDto.getTransactionAccountNumber())
				.transactionStatus(Status.ACTIVE)
				.build();
		transactionRepository.save(transaction);
		System.out.println("Transaction saved successfully");
	}
}
