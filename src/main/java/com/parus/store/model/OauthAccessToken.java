package com.parus.store.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="oauth_access_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OauthAccessToken extends DateAudit{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="authentication_id")
	private String authentication_id;

	@Basic(optional=false)
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
	
	@Basic(optional=false)
	@Column(name="refresh_token")
	private String refresh_token;
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private OAuthClientDetails oauthClientDetails;
	
	@ManyToOne
	@JoinColumn(name="user_name",referencedColumnName="user_name")
	private User user;
	
	
}
