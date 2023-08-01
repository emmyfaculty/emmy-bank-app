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
public class EmailDetails {
	@Schema(
			description = "Sender's Email Address",
			example = "aholemmy@gmail.com"
	)
	private String recipient;
	@Schema(
			description = "Message Body",
			example = "Hello Emmy Bank"
	)
	private String messageBody;
	@Schema(
			description = "Subject",
			example = "Emmy Bank"
	)
	private String subject;
	@Schema(
			description = "Attachment",
			example = "Emmy Bank"
	)
	private String attachment;

}
