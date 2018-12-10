package com.parus.store.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import com.parus.store.model.PasswordResetToken;
import com.parus.store.model.Token;

public class OnGenerateResetLinkEvent extends ApplicationEvent {
	private UriComponentsBuilder redirectUrl;
	private Token passwordResetToken;

	public OnGenerateResetLinkEvent(Token passwordResetToken, UriComponentsBuilder redirectUrl) {
		super(passwordResetToken);
		this.passwordResetToken = passwordResetToken;
		this.redirectUrl = redirectUrl;
	}

	public Token getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(Token passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public UriComponentsBuilder getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
