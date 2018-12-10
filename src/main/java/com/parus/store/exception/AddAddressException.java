package com.parus.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AddAddressException extends RuntimeException{
	
	private String message;

	public AddAddressException( String message) {
		super(String.format("Error: [%s]", message));
		this.message = message;
	}

}
