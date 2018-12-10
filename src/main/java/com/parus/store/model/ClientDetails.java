package com.parus.store.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="clientdetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="ClientDetails")
public class ClientDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "appId")
	private String appId;
	
	@Basic(optional = false)
	@Column(name = "resourceIds")
	private String resourceIds;
	
	@Basic(optional = false)
	@Column(name = "appSecret")
	private String appSecret;
	
	@Basic(optional = false)
	@Column(name = "scope")
	private String scope;
	
	@Basic(optional = false)
	@Column(name = "grantTypes")
	private String grantTypes;
	
	@Basic(optional = false)
	@Column(name = "redirectUrl")
	private String redirectUrl;
	
	@Basic(optional = false)
	@Column(name = "authorities")
	private String authorities;
	
	@Basic(optional = false)
	@Column(name = "access_token_validity")
	private int access_token_validity;
	
	@Basic(optional = false)
	@Column(name = "refresh_token_validity")
	private int refresh_token_validity;
	
	@Basic(optional = false)
	@Column(name = "additionalInformation")
	private String additionalInformation;
	
	@Basic(optional = false)
	@Column(name = "autoApproveScopes")
	private String autoApproveScopes;
}
