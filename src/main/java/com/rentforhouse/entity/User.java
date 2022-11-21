package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "username", nullable = false, unique = true)
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "phone", unique = true)
	private String phone;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "image")
	private String image;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<House> houses = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
	private List<Role> roles = new ArrayList<>();

}
