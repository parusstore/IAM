package com.parus.store.service;


import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.parus.store.event.OnGenerateResetLinkEvent;
import com.parus.store.event.OnUserRegistrationCompleteEvent;
import com.parus.store.exception.InvalidTokenRequestException;
import com.parus.store.exception.ResourceAlreadyInUseException;
import com.parus.store.exception.UpdatePasswordException;
import com.parus.store.model.Token;
import com.parus.store.model.TokenName;
import com.parus.store.model.User;
import com.parus.store.payload.PasswordResetLinkRequest;
import com.parus.store.payload.PasswordResetRequest;
import com.parus.store.payload.RegistrationRequest;
import com.parus.store.payload.UpdatePasswordRequest;


@Service
public class PreLoginUserService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private static final Logger logger = Logger.getLogger(PreLoginUserService.class);
	
	public Optional<User> registerUser(RegistrationRequest newRegistrationRequest)
	{
		String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
		if(emailAlreadyExists(newRegistrationRequestEmail))
		{
			logger.error("Email already exists: " + newRegistrationRequestEmail);
			throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
		}
		User newUser = userService.createUser(newRegistrationRequest);
		User registeredNewUser = userService.save(newUser);
		return Optional.ofNullable(registeredNewUser);
	}
	
	public Boolean emailAlreadyExists(String email) {
		return userService.existsByEmail(email);
	}

	public User changePasswordForUser(User user,String password)
	{
		return userService.changePasswordForUser(user,password);
	}
	public User confirmEmailRegistration(String token, String useremail) {
		// TODO Auto-generated method stub
		Optional<User> userOptional = userService.registerUser(token,useremail,TokenName.VERIFY_EMAIL);
		userOptional.orElseThrow(() -> new InvalidTokenRequestException("provided for email "+useremail, token,
				"Failed to confirm. Please resend email verification request"));
		User user = userOptional.get();
		user.setActive(true);
		user.setIsAccountNonExpired(true);
		user.setIsEmailVerified(true);
		user.setIsAccountNonLocked(true);
		user.setIsCredentialsNonExpired(true);
		user.setUpdatedAt(Instant.now());
		return userService.save(user);	
		
	}

	
/**	public void sendEmailVerificationPostRegistrattion(Optional<User> registeredUserOpt)
	{
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");
		//token="+token.getValue()+"&email="+registrationRequest.getEmail()
		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUserOpt.get(), urlBuilder);
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
		//there is no point in returning true the reason being we are making async request.
	}**/

/**	public void sendEmailForPasswordreset(Optional<Token> TokenUserOpt)
	{
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/password/reset");
		//token="+token.getValue()+"&email="+registrationRequest.getEmail()
		OnGenerateResetLinkEvent onGenerateResetLinkEvent =
				new OnGenerateResetLinkEvent(TokenUserOpt.get(), urlBuilder);
		applicationEventPublisher.publishEvent(onGenerateResetLinkEvent);
	}**/
	
	public Optional<Token> generatePasswordResetToken(@Valid PasswordResetLinkRequest passwordResetLinkRequest) {
		// TODO Auto-generated method stub
		Optional<Token> tokenOpt = userService.generatePasswordResetToken(passwordResetLinkRequest.getEmail(),TokenName.FORGOT_PASSWORD);
		return tokenOpt;
	}

	public Optional<User> resetPassword(@Valid PasswordResetRequest passwordResetRequest) {
		// TODO Auto-generated method stub
		Optional<User> userOpt= userService.resetPassword(passwordResetRequest);
		return userOpt;
	}


	
	public Optional<User> updatePassword(Map<String, Object> details,
			UpdatePasswordRequest updatePasswordRequest) {
		User currentUser = userService.getLoggedInUser((String)details.get("user_name"));

		if (!userService.currentPasswordMatches(currentUser, updatePasswordRequest.getOldPassword())) {
			logger.info("Current password is invalid for [" + currentUser.getPassword() + "]");
			throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
		}
		if (updatePasswordRequest.getOldPassword().equals(updatePasswordRequest.getNewPassword())) {
			logger.info("Newpassword and OldPassword cannot be the same");
			throw new UpdatePasswordException(currentUser.getEmail(), "Newpassword and OldPassword cannot be the same");
		}
		
		userService.changePasswordForUser(currentUser, updatePasswordRequest.getNewPassword());
		return Optional.ofNullable(currentUser);
	}
	
}
