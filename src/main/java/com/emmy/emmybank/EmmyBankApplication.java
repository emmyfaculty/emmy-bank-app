package com.emmy.emmybank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Emmy Bank API",
				version = "1.0",
				description = "Emmy Bank Back-end Rest API Documentation v1.0",
				contact = @Contact(
						name = "Emmanuel Ahola",
						email = "aholemmy@gmail.com",
						url = "https://github.com/emmyfaculty/emmy-bank"
			),
			license = @License(
					name = "emmy-bank",
					url = "https://github.com/emmyfaculty/emmy-bank"
			)
		),
		externalDocs = @ExternalDocumentation(
				description = "Emmy Bank Back-end Rest API Documentation v1.0",
				url = "https://github.com/emmyfaculty/emmy-bank"
		)
)
public class EmmyBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmmyBankApplication.class, args);
	}

}
