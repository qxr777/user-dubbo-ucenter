package edu.whut.cs.jee.ucenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author qixin
 */
@Entity
@Table(name="role")
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
@NoArgsConstructor
public class Role implements Serializable {
	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

	@Column(name="ROLE_NAME")
	private String name;

	@Column(name="ROLE_DESCRIPTION")
	private String description;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonIgnore
	private Set<User> users;

	public static final int MAX_NUMBER = 5;
}
