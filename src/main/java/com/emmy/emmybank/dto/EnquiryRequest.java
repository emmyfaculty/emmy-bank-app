package com.emmy.emmybank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryRequest {

	@Schema(
			description = "User Account Number",
			example = "1234567890"
	)
	private String accountNumber;


}
