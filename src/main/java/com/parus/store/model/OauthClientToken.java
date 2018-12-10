package com.parus.store.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="oauth_client_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OauthClientToken extends DateAudit
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="authentication_id")
	private String authentication_id;
	
	@Basic(optional=false)
	@Column(name="token_id")
	private String token_id;
	
	@Basic(optional=false,fetch = FetchType.LAZY)
	@Column(name="token")
	@Lob
	private byte[] token;
	
	/**@Basic(optional=false)
	@Column(name="user_name")
	private String user_name;
	
	
	 * Achived the same by using the jpa mapping
	 * @Basic(optional=false)
	@Column(name="client_id")
	private String client_id;**/
	
	@ManyToOne
	@JoinColumn(name="user_name",referencedColumnName="user_name")
	private User user;
	
	
	@ManyToOne
	@JoinColumn(name="client_id")
	private OAuthClientDetails oauthClientDetails;
	


}
