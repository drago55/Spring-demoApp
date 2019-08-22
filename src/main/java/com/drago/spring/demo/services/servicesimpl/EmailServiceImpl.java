package com.drago.spring.demo.services.servicesimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.drago.spring.demo.services.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.from.email}")
	private String from;

	@Override
	@Async
	public void sendEmail(String to, String subject, String text) {
		log.info("Sending mail...");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setFrom(from);
		message.setText(text);
		emailSender.send(message);
		log.info("Mail sent.");
	}

}
