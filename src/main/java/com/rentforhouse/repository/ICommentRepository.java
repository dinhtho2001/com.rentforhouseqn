package com.rentforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.Comment;

public interface ICommentRepository extends JpaRepository<Comment, Long>{

}
