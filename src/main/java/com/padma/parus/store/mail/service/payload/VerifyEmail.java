package com.padma.parus.store.mail.service.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Verify Email Request", description = "The payload which accepts user email and sends verify email request payload")
public class VerifyEmail  implements MailPayload{

	@NotBlank(message = "Old password must not be blank")
	@ApiModelProperty(value = "Email verrification sent to user address", required = true, allowableValues = "NonEmpty String")
	private String emailVerificationUrl;


	@NotBlank(message = "New password must not be blank")
	@ApiModelProperty(value = "Valid to address is a must", required = true, allowableValues = "NonEmpty String")
	private String to;
	
	public String getEmailVerificationUrl() {
		return emailVerificationUrl;
	}

	public void setEmailVerificationUrl(String emailVerificationUrl) {
		this.emailVerificationUrl = emailVerificationUrl;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}


	public VerifyEmail(String emailVerificationUrl, String to) {
		this.emailVerificationUrl = emailVerificationUrl;
		this.to = to;
	}

	public VerifyEmail() {
	}

	
}
