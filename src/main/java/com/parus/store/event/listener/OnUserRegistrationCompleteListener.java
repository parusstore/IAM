package com.parus.store.event.listener;

import com.padma.parus.store.mail.service.payload.VerifyEmail;
import com.parus.store.event.OnUserRegistrationCompleteEvent;
import com.parus.store.exception.MailSendException;
import com.parus.store.model.Token;
import com.parus.store.model.TokenName;
import com.parus.store.model.User;
import com.parus.store.service.MailService;
import com.parus.store.service.TokenService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableAsync
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {


	private static final Logger logger = Logger.getLogger(OnUserRegistrationCompleteListener.class);
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private MailService mailService;
	/**
	 * As soon as a registration event is complete, invoke the email verification
	 * asynchronously in an another thread pool
	 */
	@Override
	@Async
	public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
		sendEmailVerification(onUserRegistrationCompleteEvent);
	}

	/**
	 * Send email verification to the user and persist the token in the database.
	 */
	//check : need to implement server to server authentication will do it later. This is with regards to mail server

	private void sendEmailVerification(OnUserRegistrationCompleteEvent event) {
		User user = event.getUser();
		Token token=tokenService.getTokenAfterVerifyingForExpiryAndFailedAttempts(user, TokenName.VERIFY_EMAIL);
		String recipientAddress = user.getEmail();
		//need to fix this properly
		String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", token.getValue()).toUriString();
		
		try {
			mailService.sendEmailVerification(new VerifyEmail(emailConfirmationUrl, recipientAddress));
		} catch (Exception e) {
			//logger.error(e);
			//throw new MailSendException(recipientAddress, "Email Verification");
			e.printStackTrace();
		}
	}
}
