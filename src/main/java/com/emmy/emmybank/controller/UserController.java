package com.emmy.emmybank.controller;

import com.emmy.emmybank.dto.BankResponse;
import com.emmy.emmybank.dto.CreditDebitRequest;
import com.emmy.emmybank.dto.EnquiryRequest;
import com.emmy.emmybank.dto.TransferRequest;
import com.emmy.emmybank.dto.UserRequest;
import com.emmy.emmybank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs", description = "The user API")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	@Operation(summary = "Create a new user account", description = "This API creates a new user account and assigne an account number to the user"
			+ "The account number is a 10 digit number")
	@ApiResponse(responseCode = "201",
			description = "Http status code 201 is returned when a new user account is created successfully"
			+ "The response body contains the account number of the user created")
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@GetMapping("/balanceEnquiry")
	@Operation(summary = "Balance Enquiry",
			description = "Given an account number, this API returns the account balance of the user")
	@ApiResponse(responseCode = "200",
			description = "Http status code 200 is returned if the account exist successfully"
					+ "The response body contains the account balance of the user")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		return userService.balanceEnquiry(request);
	}

	@GetMapping("/nameEnquiry")
	@Operation(summary = "Name Enquiry",
			description = "Given an account number, this API returns the account name of the user")
	@ApiResponse(responseCode = "200",
			description = "Http status code 200 is returned if the account exist successfully"
					+ "The response body contains the account name of the user")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}

	@PostMapping("/creditAccount")
	@Operation(summary = "Credit Account",
			description = "Given an account number and amount, this API credits the user account with the amount")
	@ApiResponse(responseCode = "201",  description = "Http status code 201 is returned and account credited if the account exist successfully"
			+ "The response body contains the account balance of the user")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@PostMapping("/debitAccount")
	@Operation(summary = "Debit Account",
			description = "Given an account number and amount, this API debits the user account with the amount")
	@ApiResponse(responseCode = "201",  description = "Http status code 201 is returned and account debited if the account exist successfully"
			+ "The response body contains the account balance of the user")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest debitRequest) {
		return userService.debitAccount(debitRequest);
	}

	@PostMapping("/transfer")
	@Operation(summary = "Transfer",
			description = "Given an account number and amount, this API transfers the amount from the user account to the beneficiary account")
	@ApiResponse(responseCode = "201",  description = "Http status code 201 is returned and account debited if the account exist successfully"
			+ "The response body contains the account balance of the user")
	public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
		return userService.transfer(transferRequest);
	}

}
