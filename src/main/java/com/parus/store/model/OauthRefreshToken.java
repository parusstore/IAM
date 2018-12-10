package com.parus.store.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="oauth_refresh_token")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OauthRefreshToken extends DateAudit{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="token_id")
	private String token_id;
	
	@Basic(optional=false)
	@Column(name="token")
	@Lob
	private byte[] token;
	
	@Basic(optional=false)
	@Column(name="authentication")
	@Lob
	private byte[] authentication;
	
	

}
