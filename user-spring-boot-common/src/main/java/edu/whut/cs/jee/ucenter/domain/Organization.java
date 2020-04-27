package edu.whut.cs.jee.ucenter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="organization")
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
@NoArgsConstructor
@NamedEntityGraph(name = "Organization.lazy", attributeNodes = {@NamedAttributeNode("users")})
public class Organization implements Serializable {
	@Id
	@Column(name = "ORGANIZATION_ID")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

	@Column(name="ORGANIZATION_NAME")
	private String name;

	@Column(name="ORGANIZATION_DESCRIPTION")
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name="USER_ORGANIZATION_ID")
	@JsonIgnore
	private Set<User> users;

	public static final int MAX_NUMBER = 5;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Organization)) return false;
		return id == ((Organization) o).id;
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
