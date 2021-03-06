package com.drago.spring.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = { "roles", "markers", "images" })
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	public User() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NonNull
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NonNull
	@NotEmpty
	@Email
	private String email;

	@NonNull
	@NotEmpty
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_images", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
	private Set<Image> images = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_marker", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "marker_id"))
	@Transient
	private Set<Marker> markers = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

}
