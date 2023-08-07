package com.emmy.emmybank.serviceimpl;

import com.emmy.emmybank.dto.EmailDetails;
import com.emmy.emmybank.entity.Transaction;
import com.emmy.emmybank.entity.User;
import com.emmy.emmybank.repository.TransactionRepository;
import com.emmy.emmybank.repository.UserRepository;
import com.emmy.emmybank.service.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	private TransactionRepository transactionRepository;
	private UserRepository userRepository;
	private EmailService emailService;
	private static final String FILE = "C:\\Users\\Emmanuel\\Desktop\\bankStatement.pdf";
	/**
	 * retrieve lists of transactions within a date range given an account number
	 * generate a pdf file of the transactions
	 * send the pdf file to the customer's email address
	 */

	public List<Transaction> generateBankStatement(String accountNumber, String startDate, String endDate)
			throws FileNotFoundException, DocumentException {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionsList = transactionRepository.findAll().stream().filter(transaction -> transaction.getTransactionAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isEqual(end))
				.toList();
		User user = userRepository.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getOtherName() + " " + user.getLastName();

		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting size of document");
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("Emmy Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);

		PdfPCell bankAddress = new PdfPCell(new Phrase("No 1, Emmy Bank Street, Lagos"));
		bankAddress.setBorder(0);

		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);

		PdfPTable statementInfoTable = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
		customerInfo.setBorder(0);
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT "));
		statement.setBorder(0);
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
		stopDate.setBorder(0);
		PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
		name.setBorder(0);
		PdfPCell space = new PdfPCell();
		space.setBorder(0);
		PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
		address.setBorder(0);

		PdfPTable transactionsTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("Date"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell amount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		amount.setBackgroundColor(BaseColor.BLUE);
		amount.setBorder(0);
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);

		transactionsTable.addCell(date);
		transactionsTable.addCell(amount);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(status);

		transactionsList.forEach(transaction -> {
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionAmount().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionStatus().toString()));
		});

		statementInfoTable.addCell(customerInfo);
		statementInfoTable.addCell(statement);
		statementInfoTable.addCell(stopDate);
		statementInfoTable.addCell(name);
		statementInfoTable.addCell(space);
		statementInfoTable.addCell(address);

		document.add(bankInfoTable);
		document.add(statementInfoTable);
		document.add(transactionsTable);
		document.close();

		EmailDetails emailDetails = EmailDetails.builder()
				.recipient(user.getEmail())
				.subject("STATEMENT OF ACCOUNT")
				.messageBody("Please find attached your bank statement")
				.attachment(FILE)
				.build();
		emailService.sendEmailWithAttachment(emailDetails);




		return transactionsList;


	}

}
