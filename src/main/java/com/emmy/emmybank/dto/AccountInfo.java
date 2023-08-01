package com.emmy.emmybank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

	@Schema(
			description = "User Account Name",
			example = "Emmanuel Ahola"
	)
	private String accountName;
	@Schema(
			description = "User Account Balance",
			example = "1000.00"
	)
	private BigDecimal accountBalance;
	@Schema(
			description = "User Account Number",
			example = "1234567890"
	)
	private String accountNumber;

}
