package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rentforhouse.entity.Comment;
import com.rentforhouse.entity.House;

public interface ICommentRepository extends JpaRepository<Comment, Long>{

	@Query(value = "SELECT * FROM comment u WHERE u.house_id = ?1", 
			  nativeQuery = true)
	List<Comment> findAllByHouse_id(Long id);
}
