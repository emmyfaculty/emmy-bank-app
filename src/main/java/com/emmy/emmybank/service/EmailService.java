package com.emmy.emmybank.service;

import com.emmy.emmybank.dto.EmailDetails;

public interface EmailService{
	void sendEmailAlert (EmailDetails emailDetails);
}
