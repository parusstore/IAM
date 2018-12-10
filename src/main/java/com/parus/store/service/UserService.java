package com.parus.store.service;

import java.security.cert.PKIXRevocationChecker.Option;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.parus.store.config.jpa.repository.TokenRepository;
import com.parus.store.config.jpa.repository.UserRepository;
import com.parus.store.event.OnGenerateResetLinkEvent;
import com.parus.store.event.OnUserRegistrationCompleteEvent;
import com.parus.store.exception.InvalidTokenRequestException;
import com.parus.store.exception.TokenResetException;
import com.parus.store.model.Role;
import com.parus.store.model.RoleName;
import com.parus.store.model.Token;
import com.parus.store.model.TokenName;
import com.parus.store.model.User;
import com.parus.store.payload.PasswordResetRequest;
import com.parus.store.payload.RegistrationRequest;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired 
	private TokenService tokenService;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	private static final Logger logger = Logger.getLogger(UserService.class);
	
	/**
	 * Save the user to the database
	 */
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public Boolean existsByEmail(String email)
	{
		return userRepository.existsByEmail(email);
	}
	
	public User createUser(RegistrationRequest registerRequest) {
		User newUser = new User();
		//Danger:The below boolean is very dangerous and needs to be added just n case where we need. Otherwise stay away from it
		Boolean isNewUserAsAdmin = registerRequest.getRegisterAsAdmin();
		newUser.setEmail(registerRequest.getEmail());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		newUser.setUsername(registerRequest.getEmail());
		newUser.setRoles(getRolesForNewUser(isNewUserAsAdmin));
		//Danger: disable below line after implementing the token check
		newUser.setActive(true);
		newUser.setIsAccountNonExpired(false);
		newUser.setIsAccountNonLocked(false);
		newUser.setIsCredentialsNonExpired(false);
		newUser.setIsEmailVerified(false);
		newUser.setCreatedAt(Instant.now());
		newUser.setUpdatedAt(Instant.now());		
		return newUser;
	}
	
	
	
	
	/**
	 * Performs a quick check to see what roles the new user could benefit from
	 * @return list of roles for the new user
	 */
	private Set<Role> getRolesForNewUser(Boolean isAdmin) {
		Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
		Role adminRole = new Role(RoleName.ROLE_ADMIN);
		if (!isAdmin) {
			newUserRoles.remove(adminRole);
		}
		logger.info("Setting user roles: " + newUserRoles);
		return newUserRoles;
	}
	
	public void sendRegisterEmailForToken(Optional<User> registeredUserOpt)
	{
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth" +
				"/registrationConfirmation");
		//token="+token.getValue()+"&email="+registrationRequest.getEmail()
		OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
				new OnUserRegistrationCompleteEvent(registeredUserOpt.get(), urlBuilder);
		applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
	}

	
	public void sendResetPasswordEmailForToken(Token  passwordResetToken)
	{
		UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
		OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
				urlBuilder);
		applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
	}

	
	public Optional<User> registerUser(String tokenValue, String userEmail,TokenName tokeName) {
		// TODO Auto-generated method stub
		Optional<User> userOptional = userRepository.findByEmail(userEmail);
		userOptional.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", tokenValue,
				"Failed to confirm. Please resend email verification request"));
		Optional<Token> userToken = tokenRepository.findByUserWithTokenPurpose(userOptional.get(), tokeName);		
		Token token = userToken.get();
		if(userToken.isPresent())
		{
			if(!token.getValue().equals(tokenValue))
			{
				tokenService.incrementFailedCounter(token);								
				Token verifiedToken = tokenService.getTokenAfterVerifyingForExpiryAndFailedAttempts(userOptional.get(), tokeName);
				if(verifiedToken.getValue()!=token.getValue())
				{
					sendRegisterEmailForToken(userOptional);
					throw new TokenResetException(userEmail, "New one has been generated and sent you emailid. For the next attempts use the new ones.");				
				}
				return Optional.ofNullable(null);
			}
			tokenService.deleteToken(token);
			return userOptional;
		}
	 return Optional.ofNullable(null);
	}

	public Optional<User> findByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
	public Optional<Token> generatePasswordResetToken(String email, TokenName tokenName) {
		// TODO Auto-generated method stub
		Optional<User> userOpt= findByEmail(email);
		Token token =  tokenService.getTokenAfterVerifyingForExpiryAndFailedAttempts(userOpt.get(), tokenName);
		
		return Optional.ofNullable(token);
	}

	public Optional<User> resetPassword(@Valid PasswordResetRequest passwordResetRequest) {
		Optional<User> userOptional= findByEmail(passwordResetRequest.getUserEmail());
		userOptional.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", passwordResetRequest.getToken(),
				"Failed to confirm. Please resend password reset request"));
		Optional<Token> tokenOptional = tokenRepository.findByUserWithTokenPurpose(userOptional.get(), TokenName.FORGOT_PASSWORD);
		Token token = tokenOptional.get();
		if(tokenOptional.isPresent())
		{
			if(!token.getValue().equals(passwordResetRequest.getToken()))
			{
				tokenService.incrementFailedCounter(token);								
				Token verifiedToken = tokenService.getTokenAfterVerifyingForExpiryAndFailedAttempts(userOptional.get(), TokenName.FORGOT_PASSWORD);
				if(verifiedToken.getValue()!=token.getValue())
				{
					sendResetPasswordEmailForToken(verifiedToken);
					throw new TokenResetException(passwordResetRequest.getUserEmail(), "New one has been generated and sent you emailid. For the next attempts use the new ones.");
				}
				return Optional.ofNullable(null);
			}
			tokenService.deleteToken(token);
			return userOptional;
		}
	 return Optional.ofNullable(null);
	}

	public User changePasswordForUser(User user, String password) {
		// TODO Auto-generated method stub
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
		return user;
	}

	public Boolean currentPasswordMatches(User currentUser, String password) {
		return passwordEncoder.matches(password, currentUser.getPassword());
	}
	public User getLoggedInUser(String userName) {
		return userRepository.findByUsername(userName);
	}
}
