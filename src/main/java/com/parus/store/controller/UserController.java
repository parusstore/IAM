package com.parus.store.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.parus.store.exception.PasswordResetException;
import com.parus.store.exception.UpdatePasswordException;
import com.parus.store.event.OnGenerateResetLinkEvent;
import com.parus.store.event.OnUserAccountChangeEvent;
import com.parus.store.event.OnUserRegistrationCompleteEvent;
import com.parus.store.exception.PasswordResetLinkException;
import com.parus.store.exception.UserRegistrationException;
import com.parus.store.model.Token;
import com.parus.store.model.User;
import com.parus.store.payload.ApiResponse;
import com.parus.store.payload.PasswordResetLinkRequest;
import com.parus.store.payload.PasswordResetRequest;
import com.parus.store.payload.RegistrationRequest;
import com.parus.store.payload.UpdatePasswordRequest;
import com.parus.store.service.PreLoginUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.log4j.Logger;

@Controller
@RequestMapping(name="/api/auth")
public class UserController {
	
	@Autowired
	private PreLoginUserService  preLoginService;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private PreLoginUserService preLoginUserService;
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	
	@GetMapping("/me")
	//@PreAuthorize("#oauth2.hasScope('read')")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Returns the current user profile")
	public ResponseEntity<?> getUserAuthenticatedProfile(Authentication auth) {
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
        System.out.println("User organization is " + details.get("organization"));
        return ResponseEntity.ok(new ApiResponse(details.toString(), true));
	}

	
	@PostMapping("/register")
	@ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
	public ResponseEntity<?> registerUser(@ApiParam(value = "The RegistrationRequest payload") @Valid @RequestBody RegistrationRequest registrationRequest,
			WebRequest request) {
		Optional<User> registeredUserOpt = preLoginService.registerUser(registrationRequest);
		registeredUserOpt.orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
				"Missing user object in database"));
		//TRigerring email to perform required of confirmation email on registration
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");
		//token="+token.getValue()+"&email="+registrationRequest.getEmail()
		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUserOpt.get(), urlBuilder);
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
		//there is no point in returning true the reason being we are making async request.
		//preLoginUserService.sendEmailVerificationPostRegistrattion(registeredUserOpt);
		
		registeredUserOpt.ifPresent(user -> logger.info("Registered User returned [API[: " + user));
		return ResponseEntity.ok(new ApiResponse("User registered successfully. Check your email" +
				" for verification", true));
	}
	
	@GetMapping("/registrationConfirmation")
	@ApiOperation(value = "Confirms the email verification token that has been generated for the user during " +
			"registration")
	public ResponseEntity<?>  verifyEmail(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token,
			@ApiParam(value = "the email against which the token will be validated") @RequestParam("useremail") String useremail)
	{
		User verifiedUserOpt = preLoginUserService.confirmEmailRegistration(token,useremail);
		return ResponseEntity.ok(new ApiResponse("User verified successfully", true));
	}

	
	@PostMapping("/password/resetlink")
	@ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
			"reset link")
	public ResponseEntity<?> resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest) {
		Optional<Token> passwordResetTokenOpt = preLoginService
				.generatePasswordResetToken(passwordResetLinkRequest);
		passwordResetTokenOpt.orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(),
				"Couldn't create a valid token"));
		Token passwordResetToken = passwordResetTokenOpt.get();
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
		OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
				urlBuilder);
		applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
		return ResponseEntity.ok(new ApiResponse("Password reset link sent successfully", true));
	}
	
	
	/**
	 * Receives a new passwordResetRequest and sends the acknowledgement after
	 * changing the password to the user's mail through the event.
	 */

	@PostMapping("/password/reset")
	@ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
			"email")
	public ResponseEntity<?> resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
		Optional<User> userOpt = preLoginService.resetPassword(passwordResetRequest);
		userOpt.orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting " +
				"password"));
		User changedUser = userOpt.get();
		OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
				"Changed Successfully");
		applicationEventPublisher.publishEvent(onPasswordChangeEvent);
		preLoginService.changePasswordForUser(changedUser,passwordResetRequest.getPassword());
		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}
	
	/**
	 * Updates the password of the current logged in user
	 */
	@PostMapping("/password/update")
	@PreAuthorize("hasRole('USER')")
	@ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
			"password")
	public ResponseEntity<?> updateUserPassword(Authentication auth,
			@ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
		OAuth2AuthenticationDetails oauthDetails = (OAuth2AuthenticationDetails) auth.getDetails();
		Map<String, Object> details = (Map<String, Object>) oauthDetails.getDecodedDetails();
		User updatedUser = preLoginService.updatePassword(details, updatePasswordRequest)
				.orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));

		OnUserAccountChangeEvent onUserPasswordChangeEvent =
				new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
		applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);

		return ResponseEntity.ok(new ApiResponse("Password changed successfully", true));
	}
	
	
	/**
	 * Danger:JWT does not require logout. Hence jwt token needs to be deleted once user clicks on logout on client side.
	 */

}
