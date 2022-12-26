package com.rentforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long>{
	
	/* Lấy role theo Name */
	Role findByName(String code);

}
