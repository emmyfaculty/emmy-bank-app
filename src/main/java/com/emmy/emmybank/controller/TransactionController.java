package com.emmy.emmybank.controller;

import com.emmy.emmybank.entity.Transaction;
import com.emmy.emmybank.serviceimpl.BankStatement;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankStatement")
@RequiredArgsConstructor
public class TransactionController {

	private final BankStatement bankStatement;

	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
																									@RequestParam String startDate,
																									@RequestParam String endDate)
			throws DocumentException, FileNotFoundException {
		return bankStatement.generateBankStatement(accountNumber, startDate, endDate);
	}
}
