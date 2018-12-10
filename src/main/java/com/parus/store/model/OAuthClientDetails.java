package com.parus.store.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "oauth_client_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class OAuthClientDetails extends DateAudit {

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "client_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String client_id;
	
	@Column(name = "resource_ids")
	private String resource_ids;
	
	@Basic(optional = false)
	@Column(name = "client_secret")
	private String client_secret;
	
	@Basic(optional = false)
	@Column(name = "scope")
	private String scope;
	
	@Basic(optional = false)
	@Column(name = "authorized_grant_types")
	private String authorized_grant_types;
	
	//@Basic(optional = false)
	@Column(name = "web_server_redirect_uri")
	private String web_server_redirect_uri;
	
	//@Basic(optional = false)
	@Column(name = "authorities")
	private String authorities;
	
	@Basic(optional = false)
	@Column(name = "access_token_validity")
	private int access_token_validity;
	
	@Basic(optional = false)
	@Column(name = "refresh_token_validity")
	private int refresh_token_validity;
	
	//@Basic(optional = false)
	@Column(name = "additional_information")
	private String additional_information ;
	
	@Basic(optional = false)
	@Column(name = "autoapprove")
	private String autoapprove;
	
	@OneToMany(mappedBy="oauthClientDetails",cascade=CascadeType.ALL)
	private Set<OauthClientToken> oauthClientTokens;
	
	@OneToMany(mappedBy="oauthClientDetails",cascade=CascadeType.ALL)
	private Set<OauthAccessToken> oauthAccessTokens;
}
