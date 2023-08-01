package com.emmy.emmybank.serviceimpl;

import com.emmy.emmybank.constants.Status;
import com.emmy.emmybank.dto.AccountInfo;
import com.emmy.emmybank.dto.BankResponse;
import com.emmy.emmybank.dto.CreditDebitRequest;
import com.emmy.emmybank.dto.EmailDetails;
import com.emmy.emmybank.dto.EnquiryRequest;
import com.emmy.emmybank.dto.TransactionDto;
import com.emmy.emmybank.dto.TransferRequest;
import com.emmy.emmybank.dto.UserRequest;
import com.emmy.emmybank.entity.User;
import com.emmy.emmybank.repository.UserRepository;
import com.emmy.emmybank.service.EmailService;
import com.emmy.emmybank.service.TransactionService;
import com.emmy.emmybank.service.UserService;
import com.emmy.emmybank.utils.AccountUtils;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService emailService;
	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/**
		 * Creating an account - saving a new user into the db
		 * check if user already has an account
		 */

		if (userRepository.existsByEmail(userRequest.getEmail())) {
			BankResponse response = BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
			return response;
		}
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status(Status.ACTIVE)
				.build();

		User savedUser = userRepository.save(newUser);
		//send email Alert
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(savedUser.getEmail())
				.subject("ACCOUNT CREATION")
				.messageBody("Congratulations! Your Account has been Successfully Created.\n Your Account Details:\n" +
						"Account Name: " + savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName() + "\nAccount Number: " + savedUser.getAccountNumber())
				.build();
		emailService.sendEmailAlert(emailDetails);
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getOtherName() + " " + savedUser.getLastName())
						.build())
				.build();
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		// check if the provided account number exists in the database
		boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}

		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
				.accountInfo(AccountInfo.builder()
						.accountBalance(foundUser.getAccountBalance())
						.accountNumber(enquiryRequest.getAccountNumber())
						.accountName(foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getOtherName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditRequest) {
		//checking if Account exists
		boolean isAccountExist = userRepository.existsByAccountNumber(creditRequest.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		//getting the user
		User userToCredit = userRepository.findByAccountNumber(creditRequest.getAccountNumber());
		 userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));
		userRepository.save(userToCredit);
		//send email Alert
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(userToCredit.getEmail())
				.subject("ACCOUNT CREDIT ALERT")
				.messageBody("Your Account has been Credited with the sum of " + creditRequest.getAmount() + " on " + new Date())
				.build();
		emailService.sendEmailAlert(emailDetails);
		//saving the transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.transactionAmount(creditRequest.getAmount())
				.transactionType("CREDIT")
				.transactionAccountNumber(userToCredit.getAccountNumber())
				.build();
		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(userToCredit.getAccountBalance())
						.accountNumber(creditRequest.getAccountNumber())
						.accountName(userToCredit.getFirstName() + " " + userToCredit.getOtherName() + " " + userToCredit.getLastName())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest debitRequest) {
		/**
		 * check if the account exists
		 * check if the account has sufficient balance i.e. if the amount to be debited is less than the account balance
		 */
		boolean isAccountExist = userRepository.existsByAccountNumber(debitRequest.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToDebit = userRepository.findByAccountNumber(debitRequest.getAccountNumber());
		if (userToDebit.getAccountBalance().compareTo(debitRequest.getAmount()) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
		userRepository.save(userToDebit);

		//send email Alert
		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(userToDebit.getEmail())
				.subject("ACCOUNT DEBIT ALERT")
				.messageBody("Your Account has been Debited with the sum of " + debitRequest.getAmount() + " on " + new Date())
				.build();
		emailService.sendEmailAlert(emailDetails);
		//saving the transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.transactionAmount(debitRequest.getAmount())
				.transactionType("CREDIT")
				.transactionAccountNumber(userToDebit.getAccountNumber())
				.build();
		transactionService.saveTransaction(transactionDto);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(userToDebit.getAccountBalance())
						.accountNumber(debitRequest.getAccountNumber())
						.accountName(userToDebit.getFirstName() + " " + userToDebit.getOtherName() + " " + userToDebit.getLastName())
						.build())
				.build();
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
		/**
		 * get the amount to be transferred
		 * check if the account is more than the current account balance
		 * check if the account exists
		 * check if the account to be transferred to exist
		 * debit the account
		 * credit the account
		 *  sends email to the account holder
		 *  sends email to the account to be credited
		 * return success message
		 */
		boolean isDestinationAccountExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
		if (!isDestinationAccountExist) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User userToDebit = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
		if (userToDebit.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
					.build();
		}
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transferRequest.getAmount()));
		String sourceAccountName = userToDebit.getFirstName() + " " + userToDebit.getOtherName() + " " + userToDebit.getLastName();
		userRepository.save(userToDebit);
		EmailDetails debitAlert = EmailDetails.builder()
				.subject("Account Debit Alert")
				.recipient(userToDebit.getEmail())
				.messageBody("Your account has been debited with " + transferRequest.getAmount() + " on " + new Date() + ". Your current balance is " + userToDebit.getAccountBalance())
				.build();
		emailService.sendEmailAlert(debitAlert);

		//saving the transaction
		TransactionDto transactionDto = TransactionDto.builder()
				.transactionAmount(transferRequest.getAmount())
				.transactionType("DEBIT")
				.transactionAccountNumber(userToDebit.getAccountNumber())
				.build();
		transactionService.saveTransaction(transactionDto);

		User userToCredit = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(transferRequest.getAmount()));
		userRepository.save(userToCredit);
		EmailDetails creditAlert = EmailDetails.builder()
				.subject("Account Credit Alert")
				.recipient(userToCredit.getEmail())
				.messageBody("Your account has been credited with " + transferRequest.getAmount() + " From " + sourceAccountName + " on " + new Date() + ". Your current balance is " + userToCredit.getAccountBalance())
				.build();
		emailService.sendEmailAlert(creditAlert);

		//saving the transaction
		TransactionDto transactionDto2 = TransactionDto.builder()
				.transactionAmount(transferRequest.getAmount())
				.transactionType("CREDIT")
				.transactionAccountNumber(userToDebit.getAccountNumber())
				.build();
		transactionService.saveTransaction(transactionDto2);

		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(userToDebit.getAccountBalance())
						.accountNumber(transferRequest.getSourceAccountNumber())
						.accountName(sourceAccountName)
						.build())
				.build();
	}
}
