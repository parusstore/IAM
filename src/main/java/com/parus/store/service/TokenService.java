package com.parus.store.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.parus.store.config.jpa.repository.TokenRepository;
import com.parus.store.model.Token;
import com.parus.store.model.TokenName;
import com.parus.store.model.User;

//check : add exceptions where token is not found and write down your own exception for the same

@Service
@PropertySource("classpath:token.properties")
public class TokenService {
	
	@Autowired 
	private TokenRepository tokenRepository;
	
	@Value("${spring.token.expiry}")
	private int tokenLife;
	
	@Value("${spring.token.length}")
	private int tokenLength;
	
	@Value("${spring.token.allowedFailedAttempts}")
	private int allowedFailedAttempts;
	
	
	public Boolean checkIfTokenExpired(Token token)
	{
		Instant createdAt = token.getCreatedAt();
		Duration dur = Duration.between(createdAt, Instant.now());
		if(dur.toHours()>=tokenLife)
		{
			return true;
		}
		return false;
	}
	
	public void deleteToken(Token token)
	{
		tokenRepository.delete(token);
	}
	
	public Token incrementFailedCounter(Token token)
	{
		token.setNoOfFailedAttempts(token.getNoOfFailedAttempts()+1);
		tokenRepository.save(token);
		return token;
	}
	
	public Optional<Token> getIfTokenExists(User user,TokenName tokenName)
	{
		Optional<Token> optional =tokenRepository.findByUserWithTokenPurpose(user,tokenName);
		return optional;
	}
	
	//returns token of the given length
	public String generateToken(int length)
	{
		SecureRandom secureRandom = new SecureRandom();
		int result = secureRandom.nextInt(1000000);
		String resultStr = result + "";
		if (resultStr.length() != 6) 
		    for (int x = resultStr.length(); x < 6; x++) resultStr = "0" + resultStr;
		return resultStr;
	}
	
	public Token createNewToken(User user,TokenName tokenName)
	{
		Token token =new Token();
		token.setNoOfFailedAttempts(0);
		token.setTokenpurpose(tokenName);
		token.setValue(generateToken(tokenLength));
		token.setUser(user);
		token.setCreatedAt(Instant.now());
		token.setUpdatedAt(Instant.now());
		tokenRepository.save(token);
		return token;
		
	}
	
	public Boolean checkIfExcceedFailedAttempts(Token token)
	{
		if(token.getNoOfFailedAttempts() >=allowedFailedAttempts)
		{
			return true;
		}
		return false;
	}
	
	
	public Token getTokenAfterVerifyingForExpiryAndFailedAttempts(User user,TokenName tokenName)
	{
		Optional<Token> optional = getIfTokenExists(user,tokenName);
		if(optional.isPresent())
		{
			Token token= optional.get();
			if(checkIfTokenExpired(token) || checkIfExcceedFailedAttempts(token))
			{
				tokenRepository.delete(token);
				return createNewToken(user,tokenName);
			}
			else
			{
				return token;
			}

		}
		
		return createNewToken(user,tokenName);
		
	}
}
