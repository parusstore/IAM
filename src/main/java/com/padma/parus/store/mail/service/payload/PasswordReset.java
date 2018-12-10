package com.padma.parus.store.mail.service.payload;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Password Reset Request", description = "The payload contains link which helps user to reset password")
public class PasswordReset  implements MailPayload{

	@NotBlank(message = "Old password must not be blank")
	@ApiModelProperty(value = "Email verrification sent to user address", required = true, allowableValues = "NonEmpty String")
	private String resetPasswordLink;


	@NotBlank(message = "New password must not be blank")
	@ApiModelProperty(value = "Valid to address is a must", required = true, allowableValues = "NonEmpty String")
	private String to;
	


	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}



	public String getResetPasswordLink() {
		return resetPasswordLink;
	}

	public void setResetPasswordLink(String resetPasswordLink) {
		this.resetPasswordLink = resetPasswordLink;
	}
	
	public PasswordReset(String resetPasswordLink, String to) {
		this.resetPasswordLink = resetPasswordLink;
		this.to = to;
	}

	public PasswordReset() {
	}
}
