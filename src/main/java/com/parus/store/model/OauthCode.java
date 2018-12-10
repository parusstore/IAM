package com.parus.store.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name="oauth_code")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthCode extends DateAudit{
	

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@Basic(optional=false)
	@Column(name="code")
	private String code;
	
	@Basic(optional=false)
	@Column(name="authentication")
	@Lob
	private byte[] authentication;

}
