package com.emmy.emmybank.dto;

import com.emmy.emmybank.constants.Status;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
	private String transactionType;
	private BigDecimal transactionAmount;
	private String transactionAccountNumber;
	private Status transactionStatus;

}
