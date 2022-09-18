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

@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity{

	@Column(name = "fist_name")
    private String fistName;
	
	@Column(name = "last_name")
    private String lastName;
	
	@Column(name = "username", nullable = false)
    private String userName;
	
	@Column(name = "password", nullable = false)
    private String password;
	
	@Column(name = "phone")
    private String phone;
	
	@Column(name = "email")
    private String email;
	
	@OneToMany(mappedBy ="user" ,fetch = FetchType.LAZY) 
	private List<HouseEntity> houses = new ArrayList<>();
	
	 @ManyToMany(fetch = FetchType.LAZY)
	    @JoinTable(name = "user_roles",
	            joinColumns = @JoinColumn(name = "user_id", nullable = false),
	            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
	    private List<RoleEntity> roles = new ArrayList<>();

	public String getFistName() {
		return fistName;
	}

	public void setFistName(String fistName) {
		this.fistName = fistName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<HouseEntity> getHouses() {
		return houses;
	}

	public void setHouses(List<HouseEntity> houses) {
		this.houses = houses;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
	
}
