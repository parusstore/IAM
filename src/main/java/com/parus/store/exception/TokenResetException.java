
package com.parus.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CREATED)
public class TokenResetException extends RuntimeException {

	private String userEmail;
	private String message;

	public TokenResetException(String userEmail, String message) {
		super(String.format(" [%s] has exceeed allowed attempts for generated token: [%s])", userEmail, message));
		this.userEmail = userEmail;
		this.message = message;
	}
}