package com.emmy.emmybank.serviceimpl;

import com.emmy.emmybank.dto.EmailDetails;
import com.emmy.emmybank.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	//@Autowired
	private final JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		try {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setFrom(senderEmail);
				mailMessage.setTo(emailDetails.getRecipient());
				mailMessage.setText(emailDetails.getMessageBody());
				mailMessage.setSubject(emailDetails.getSubject());


				javaMailSender.send(mailMessage);
			LogManager logger = new LogManager();
			logger.log("Mail sent successfully");
		} catch (MailException e) {
				throw new RuntimeException(e);
		}
	}
}
