package com.emmy.emmybank.serviceimpl;

import com.emmy.emmybank.dto.EmailDetails;
import com.emmy.emmybank.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

	@Override
	public void sendEmailWithAttachment(EmailDetails emailDetails) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
			javaMailSender.send(mimeMessage);
			log.info(file.getFilename() + " sent successfully to user with email: " + emailDetails.getRecipient());
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
