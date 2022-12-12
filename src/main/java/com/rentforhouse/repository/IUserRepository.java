package com.rentforhouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

	User findOneById(User id);

	Optional<User> findByUserName(String username);

	Boolean existsByEmail(String email);

	Boolean existsByPhone(String number);

	Boolean existsByUserName(String username);

	Boolean existsByPassword(String password);

	List<User> findByRoles_Id(Long id);

	@Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.userName = ?1")
	Boolean existsByUserNameAndPassword(String username/* , String password */);

	@Query(nativeQuery = true, value = "SELECT * FROM User u WHERE u.phone LIKE %:content% OR u.first_name LIKE %:content% OR u.last_name LIKE %:content% OR u.username LIKE %:content% OR u.email LIKE %:content% ORDER BY u.createddate DESC", 
			      countQuery = "SELECT COUNT(id) FROM User u WHERE u.phone LIKE %:content% OR u.first_name LIKE %:content% OR u.last_name LIKE %:content% OR u.username LIKE %:content% OR u.email LIKE %:content% ORDER BY u.createddate DESC")
	Page<User> findAllByContent(@Param("content") String content, Pageable pageable);
}
