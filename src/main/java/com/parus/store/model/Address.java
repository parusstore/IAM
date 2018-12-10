package com.parus.store.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="address")
@Table(name="address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_SEQ")
    @SequenceGenerator(sequenceName = "customer_seq", allocationSize = 1, name = "CUST_SEQ")
	@Column(name = "addressId" , unique = true)
	private Long addressId;
	
	@Basic(optional = false)
	@Column(name = "addressline1")
	private String addressline1;
	
	@Basic(optional = true)
	@Column(name = "addressline2")
	private String addressline2;
	
	@Basic(optional = false)
	@Column(name = "zipCode")
	private String zipCode;
	
	@Basic(optional = false)
	@Column(name = "city")
	private String city;
	
	@Basic(optional = false)
	@Column(name = "country")
	private String country;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
