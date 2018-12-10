package com.parus.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ModifyAddressException  extends RuntimeException{
	
	private String message;
	private String addressId;

	public ModifyAddressException( String addressId,String message) {
		super(String.format("Error:[%s] for AddressId: [%s]", message,addressId));
		this.message = message;
		this.addressId = addressId;
	}

}
