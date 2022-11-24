package com.rentforhouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rentforhouse.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

	User findOneById(User id);

	Optional<User> findByUserName(String username);

	Boolean existsByEmail(String email);

	Boolean existsByPhone(String number);

	Boolean existsByUserName(String username);

	Boolean existsByPassword(String password);

	@Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1")
	Boolean existsByUserNameAndPassword(String username/* , String password */);
}
