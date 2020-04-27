package edu.whut.cs.jee.ucenter.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author qixin
 */
@Entity
@Table(name="user")
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
@NoArgsConstructor
public class User implements Serializable {
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

	@Column(name="USER_NAME")
	private String name;

	@Column(name="USER_PASSWORD")
	private String password;

	@Transient
	private String password2;

	@Column(name="USER_EMAIL")
	private String email;

	@Transient
	private long organizationId;

	@ManyToOne
	@JoinColumn(name = "USER_ORGANIZATION_ID")
	private Organization organization;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_role",
			joinColumns = { @JoinColumn(name = "USER_ROLE_USER_ID") },
			inverseJoinColumns = { @JoinColumn(name = "USER_ROLE_ROLE_ID") })
	private Set<Role> roles;

}
