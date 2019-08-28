package com.drago.spring.demo.services.servicesimpl;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.domain.VerificationToken;
import com.drago.spring.demo.exception.UserNotFoundException;
import com.drago.spring.demo.repositories.VerificatinTokenRepository;
import com.drago.spring.demo.services.EmailService;
import com.drago.spring.demo.services.SecurityService;
import com.drago.spring.demo.services.UserService;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private EmailService eMailService;

	@Autowired
	private VerificatinTokenRepository verificationTokenRepository;

	@Autowired
	private UserService userService;

	@Value("${spring.mail.message}")
	private String message;

	public String validateVerificationToken(long id, String token) {

		VerificationToken passToken = verificationTokenRepository.findByToken(token);
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			return "invalidToken";
		}

		Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "token expired";
		}

		return null;
	}

	@Override
	public void sendVerificationTokenMail(String contextPath, String userEmail) {

		User user = userService.findUserByEmail(userEmail);

		String token = UUID.randomUUID().toString();

		userService.createVerificationTokenForUser(user, token);

		String url = contextPath + "/registration/confirm/" + user.getId() + "/" + token;

		eMailService.sendEmail(user.getEmail(), "User verification mail", "Welcome to demo application, to finish registration click on link below." + " \r\n" + url);

	}
	
	@Override
	public void sendResetPasswordMail(String contextPath, String userEmail) {

		User user = userService.findUserByEmail(userEmail);

		String token = UUID.randomUUID().toString();

		userService.createVerificationTokenForUser(user, token);

		String url = contextPath + "/registration/changePassword/" + user.getId() + "/" + token;

		eMailService.sendEmail(user.getEmail(), "Reset Password", message + " \r\n" + url);

	}

	@Override
	@Transactional
	public void deleteToken(String token) {
		verificationTokenRepository.deleteByToken(token);
	}

	@Override
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Override
	public void grantUserAutority(Long id, List<GrantedAuthority> autorities) {
		if(id == null ) {
			throw new UserNotFoundException("User doesn't exist!!!");
		}
		User user = userService.findUserById(id);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, autorities);
		SecurityContextHolder.getContext().setAuthentication(auth);	
	}

}
