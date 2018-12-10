package com.parus.store.event.listener;

import com.padma.parus.store.mail.service.payload.AccountChange;
import com.parus.store.event.OnUserAccountChangeEvent;
import com.parus.store.model.User;
import com.parus.store.service.MailService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableAsync
public class OnUserAccountChangeListener implements ApplicationListener<OnUserAccountChangeEvent> {

	@Autowired
	private MailService mailService;

	private static final Logger logger = Logger.getLogger(OnUserAccountChangeListener.class);

	/**
	 * As soon as a registration event is complete, invoke the email verification
	 * asynchronously in an another thread pool
	 */
	@Override
	@Async
	public void onApplicationEvent(OnUserAccountChangeEvent onUserAccountChangeEvent) {
		sendAccountChangeEmail(onUserAccountChangeEvent);
	}

	/**
	 * Send email verification to the user and persist the token in the database.
	 */
	private void sendAccountChangeEmail(OnUserAccountChangeEvent event) {
		User user = event.getUser();
		String action = event.getAction();
		String actionStatus = event.getActionStatus();
		String recipientAddress = user.getEmail();

		try {
			mailService.sendAccountChangeEmail(new AccountChange(action, actionStatus, recipientAddress));
		} catch (Exception e) {
			//logger.error(e);
			//throw new MailSendException(recipientAddress, "Email Verification");
			e.printStackTrace();
		}
	}
}
