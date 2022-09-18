package com.rentforhouse.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

public class RoleEntity extends BaseEntity{

	 @Column(nullable = false)
	 private String name;
	
	 @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	 private List<UserEntity> users = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	 
	 
}
