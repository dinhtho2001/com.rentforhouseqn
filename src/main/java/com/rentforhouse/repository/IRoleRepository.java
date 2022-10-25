package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, Long>{
	List<Role> findByName(String code);

}
