package com.parus.store.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="oauth_approvals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OauthApprovals extends DateAudit {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "OauthApprovalId")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", allocationSize = 1)
	private String id;
	
	
	@Basic(optional = false)
	@Column(name = "clientId")
	private String clientId;
	
	@Basic(optional = false)
	@Column(name = "scope")
	private String scope;
	
	@Basic(optional = false)
	@Column(name = "status")
	private String status;
	
	@Basic(optional = false)
	@Column(name = "expiresAt", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiresAt;
	
	@Basic(optional = false)
	@Column(name = "lastModifiedAt", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedAt;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
