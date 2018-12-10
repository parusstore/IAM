package com.parus.store.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.boot.model.source.spi.FetchableAttributeSource;

import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="TOKEN")
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token extends DateAudit{
	
	@Id
	@Column(name = "TOKEN_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
	@SequenceGenerator(name = "token_seq", allocationSize = 1,initialValue=1)
	private Long id;
	
	
	@Column(name="TOKENPURPOSE")
	@Enumerated(EnumType.STRING)
	//@NaturalId
	private TokenName tokenpurpose;
	
	@Column(name="NOOFFAILEDATTEMPTS", nullable = false)
	private int noOfFailedAttempts;
	
	@Column(name="VALUE", nullable = false)
    private String value;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
