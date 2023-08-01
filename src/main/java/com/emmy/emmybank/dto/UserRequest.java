package com.emmy.emmybank.dto;

import com.emmy.emmybank.constants.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	@Schema(
			description = "User Account firstName",
			example = "Emmanuel"
	)
	private String firstName;
	@Schema(
			description = "User Account lastName",
			example = "Ahola"
	)
	private String lastName;
	@Schema(
			description = "User Account otherName",
			example = "Oluwaseun"
	)
	private String otherName;
	@Schema(
			description = "User Account Sex",
			example = "Male/Female"
	)
	private String gender;
	@Schema(
			description = "User Account Address",
			example = "No 1, Oluwaseun Street, Lagos"
	)
	private String address;
	@Schema(
			description = "User Account State of Origin",
			example = "Lagos"
	)
	private String stateOfOrigin;
	@Schema(
			description = "User Account email",
			example = "Ikeja@gmail.com"
	)
	private String email;
	@Schema(
			description = "User Account phoneNumber",
			example = "08012345678"
	)
	private String phoneNumber;
	@Schema(
			description = "User Account alternativePhoneNumber",
			example = "08012345678"
	)
	private String alternativePhoneNumber;
	@Schema(
			description = "User Account Status",
			example = "ACTIVE/INACTIVE"
	)
	private Status status;

}
