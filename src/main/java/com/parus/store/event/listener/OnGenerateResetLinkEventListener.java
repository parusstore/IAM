package com.parus.store.event.listener;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.padma.parus.store.mail.service.payload.PasswordReset;
import com.parus.store.event.OnGenerateResetLinkEvent;
import com.parus.store.model.Token;
import com.parus.store.service.MailService;

import java.io.IOException;

@Component
@EnableAsync
public class OnGenerateResetLinkEventListener implements ApplicationListener<OnGenerateResetLinkEvent> {
	@Autowired
	private MailService mailService;

	private static final Logger logger = Logger.getLogger(OnGenerateResetLinkEventListener.class);

	/**
	 * As soon as a forgot password link is clicked and a valid email id is entered,
	 * Reset password link will be sent to respective mail via this event
	 */
	@Override
	@Async
	public void onApplicationEvent(OnGenerateResetLinkEvent onGenerateResetLinkMailEvent) {
		sendResetLink(onGenerateResetLinkMailEvent);
	}

	/**
	 * Sends Reset Link to the mail address with a password reset link token
	 */
	private void sendResetLink(OnGenerateResetLinkEvent event) {
		Token token = event.getPasswordResetToken();
		String emailConfirmationUrl = event.getRedirectUrl().queryParam("token", token.getValue()).toUriString();

		try {
			mailService.sendResetLink(new PasswordReset(emailConfirmationUrl, token.getUser().getEmail()));
		} catch (Exception e) {
			//logger.error(e);
			//throw new MailSendException(recipientAddress, "Email Verification");
			e.printStackTrace();
		}
	}

}
