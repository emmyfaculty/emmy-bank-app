package com.emmy.emmybank.utils;

import java.time.Year;
import java.util.Random;

public class AccountUtils {
	private static final Random random = new Random();


		private AccountUtils() {
			throw new IllegalStateException("Utility class");
		}
	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user has already been created";
	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created";
	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account does not exist";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
	public static final String ACCOUNT_CREDITED_SUCCESS = "005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account Credited Successfully";
	public static final String ACCOUNT_DEBITED_SUCCESS = "006";
	public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User Account Debited Successfully";
	public static final String ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_CODE = "007";
	public static final String ACCOUNT_DEBITED_INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
	public static final String ACCOUNT_TRANSFER_SUCCESS_CODE = "008";
	public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "Transfer Successful";


	public static String generateAccountNumber() {

		/**
		 * accountNumber should be current year + randomSixDigit
		 */
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;

		int randNumber = random.nextInt(max - min + 1) + min;


		String year = String.valueOf(currentYear.getValue()); // Use getValue() to get the year as an int
		String randomNumber = String.format("%06d", randNumber); // Ensure the random number is 6 digits long

		return year + randomNumber;
	}

}
