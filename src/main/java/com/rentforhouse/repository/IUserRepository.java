package com.rentforhouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.User;

public interface IUserRepository extends JpaRepository<User, Long>{

	Optional<User> findById(Long id);
	User findOneById(User id);
	Optional<User> findByUserName(String username);
	Boolean existsByEmail(String email);
}
