package com.parus.store.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.padma.parus.store.mail.service.payload.MailPayload;


@Service
@PropertySource("classpath:mail-server.properties")
public class MailService {
	
	@Value("${spring.mail.server.verfiyemail}")
	private String verifyEmailLink;
	
	@Value("${spring.mail.server.resetLink}")
	private String resetLink;
	
	@Value("${spring.mail.server.accountChangeEmailLink}")
	private String accountChangeEmailLink;


	private static final Logger logger = Logger.getLogger(MailService.class);
	

	public void sendEmailVerification(MailPayload mailPayload) throws URISyntaxException
	{
		send(verifyEmailLink,mailPayload);
		 
	}

	/**
	 * Setting the mail parameters.Send the reset link to the respective user's mail
	 * @throws URISyntaxException 
	 */
	public void sendResetLink(MailPayload mailPayload) throws URISyntaxException
    {
		send(resetLink,mailPayload);
	}

	/**
	 * Send an email to the user indicating an account change event with the correct
	 * status
	 * @throws URISyntaxException 
	 */
	public void sendAccountChangeEmail(MailPayload mailPayload) throws URISyntaxException
    {
		send(accountChangeEmailLink,mailPayload);
	}

	/**
	 * Sends a simple mail as a MIME Multipart message
	 * @throws URISyntaxException 
	 */
	public static void send(String url,MailPayload passwordReset) throws URISyntaxException 
	{
		 RestTemplate restTemplate = new RestTemplate();
		 URI uri = new URI(url);
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 
		 HttpEntity<MailPayload> request = new HttpEntity<>(passwordReset, headers);
		 
		 ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);		 
	}
	
/**	public static void main(String args[])
	{
		
		try {
			PasswordReset passwordReset =  new PasswordReset("http://yahoo.com","naveen.rudra02@gmail.com");
			sendResetLink((PasswordReset)passwordReset);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}**/

}
