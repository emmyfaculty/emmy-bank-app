package com.emmy.emmybank.service;

import com.emmy.emmybank.dto.BankResponse;
import com.emmy.emmybank.dto.CreditDebitRequest;
import com.emmy.emmybank.dto.EnquiryRequest;
import com.emmy.emmybank.dto.TransferRequest;
import com.emmy.emmybank.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	String nameEnquiry(EnquiryRequest enquiryRequest);
	BankResponse creditAccount(CreditDebitRequest creditRequest);
	BankResponse debitAccount(CreditDebitRequest DebitRequest);
	BankResponse transfer(TransferRequest transferRequest);

}
