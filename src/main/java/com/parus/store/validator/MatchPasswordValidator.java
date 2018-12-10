package com.parus.store.validator;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.parus.store.annotation.MatchPassword;
import com.parus.store.payload.PasswordResetRequest;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, PasswordResetRequest> {

	private Boolean allowNull;

	@Override
	public void initialize(MatchPassword constraintAnnotation) {
		allowNull = constraintAnnotation.allowNull();
	}

	@Override
	public boolean isValid(PasswordResetRequest value, ConstraintValidatorContext context) {
		String password = value.getPassword();
		String confirmPassword = value.getConfirmPassword();
		if (allowNull) {
			return null == password && null == confirmPassword;
		}
		return password.equals(confirmPassword);
	}
}
