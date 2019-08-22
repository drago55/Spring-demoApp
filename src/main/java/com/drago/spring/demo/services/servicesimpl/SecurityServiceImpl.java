package com.drago.spring.demo.services.servicesimpl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.drago.spring.demo.domain.PasswordResetToken;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.repositories.PasswordTokenRepository;
import com.drago.spring.demo.services.EmailService;
import com.drago.spring.demo.services.SecurityService;
import com.drago.spring.demo.services.UserService;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private EmailService eMailService;

	@Autowired
	private PasswordTokenRepository passwordTokenRepository;

	@Autowired
	private UserService userService;

	@Value("${spring.mail.from.email}")
	private String message;

	public String validatePasswordResetToken(long id, String token) {

		PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			return "invalidToken";
		}

		Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expired";
		}

		User user = passToken.getUser();
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		return null;
	}

	@Override
	public void sendResetPassword(String contextPath, String userEmail) {
		
		User user = userService.findUserByEmail(userEmail);

		String token = UUID.randomUUID().toString();

		userService.createPasswordResetTokenForUser(user, token);

		String url = contextPath + "/registration/changePassword/" + user.getId() + "/" + token;

		eMailService.sendEmail(user.getEmail(), "Reset Password", message + " \r\n" + url);

	}

}
