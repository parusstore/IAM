package com.parus.store.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

import com.parus.store.annotation.MatchPassword;

@MatchPassword
@ApiModel(value = "Password reset Request", description = "The password reset request payload")
public class PasswordResetRequest {

	@NotBlank(message = "Password cannot be blank")
	@ApiModelProperty(value = "New user password", required = true, allowableValues = "NonEmpty String")
	private String password;

	@NotBlank(message = "Confirm Password cannot be blank")
	@ApiModelProperty(value = "Must match the new user password. Else exception will be thrown", required = true,
			allowableValues = "NonEmpty String matching the password")
	private String confirmPassword;

	@NotBlank(message = "Token has to be supplied along with a password reset request")
	@ApiModelProperty(value = "Reset token received in mail", required = true, allowableValues = "NonEmpty String")
	private String token;
	
	@NotBlank(message = "email has to be supplied along with a password reset request")
	@ApiModelProperty(value = "email of user", required = true, allowableValues = "NonEmpty String")
	private String userEmail;

	public PasswordResetRequest() {
	}

	public PasswordResetRequest(String password, String confirmPassword, String token,String userEmail) {
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.token = token;
		this.userEmail=userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
