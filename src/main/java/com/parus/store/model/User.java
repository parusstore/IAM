package com.parus.store.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.springframework.context.annotation.Lazy;

import com.parus.store.annotation.NullOrNotBlank;
import com.parus.store.audit.DateAudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Entity(name="users")
@Table
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateAudit {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "userId")
	//@GeneratedValue(generator="system-uuid")
	//@GenericGenerator(name="system-uuid", strategy = "uuid")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", allocationSize = 1,initialValue=2)
	private Long  userId;

	@NaturalId
	@Column(name = "email", unique = true)
	@NotBlank(message = "User email cannot be null")
	private String email;

	@Column(name = "user_name", unique = true)
	@NullOrNotBlank(message = "Username can not be blank")
	private String username;

	@Column(name = "password")
	@NotNull(message = "Password cannot be null")
	private String password;

	@Column(name = "first_name")
	@NullOrNotBlank(message = "First name can not be blank")
	private String firstName;

	@Column(name = "last_name")
	@NullOrNotBlank(message = "Last name can not be blank")
	private String lastName;

	@Column(name = "is_active", nullable = false)
	private Boolean active;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_authority", joinColumns = {
			@JoinColumn(name = "userId", referencedColumnName = "userId") }, inverseJoinColumns = {
					@JoinColumn(name = "roleId", referencedColumnName = "roleId") })
	private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<OauthApprovals> oauthApprovals;

	@Column(name = "is_email_verified", nullable = false)
	private Boolean isEmailVerified;
	
	@Column(name="is_account_non_locked",nullable=false)
	private Boolean isAccountNonLocked;
	
	@Column(name="is_credentials_non_expired", nullable=false)
	private Boolean isCredentialsNonExpired;
	
	@Column(name="is_account_non_expired")
	private Boolean isAccountNonExpired;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<OauthAccessToken> oauthAcessTokens;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<OauthClientToken> oauthClientTokens;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<Token> tokens;

	@OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	private Set<Address> addresses;
	/** the below code is dummy and not needed because we are using lombok framework
	 * public void addRole(Role role) {
		roles.add(role);
		role.getUserList().add(this);
	}

	public void addRoles(Set<Role> roles) {
		roles.forEach(this::addRole);
	}

	public void removeRole(Role role) {
		roles.remove(role);
		role.getUserList().remove(this);
	}**/


/**	public String toString() {
		return "User{" + "id=" + id + ", email='" + email + '\'' + ", username='" + username + '\'' + ", password='"
				+ password + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", active="
				+ active + ", roles=" + roles + ", isEmailVerified=" + isEmailVerified + '}';
	}**/
}
