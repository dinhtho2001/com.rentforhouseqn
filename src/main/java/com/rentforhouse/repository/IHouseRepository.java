package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentforhouse.entity.House;

public interface IHouseRepository extends JpaRepository<House, Long> {
	Page<House> findByNameLike(String name, Pageable pageable);

	@Query("SELECT u FROM House u WHERE u.name LIKE %:name% AND u.status = 1 ORDER BY u.view DESC")
	List<House> findByNameContaining(@Param("name") String name);

	Page<House> findByHouseTypes_Id(Long typeId, Pageable pageable);

	Page<House> findByNameLikeAndHouseTypes_Id(String name, Long typeId, Pageable pageable);

	@Query(value = "SELECT * FROM House u WHERE u.user_id=:userId ORDER BY u.createddate DESC", countQuery = "SELECT COUNT(id) FROM House u WHERE u.user_id=:userId ORDER BY u.createddate DESC", nativeQuery = true)
	Page<House> findByUser_Id(@Param("userId") Long userId, Pageable pageable);

	Page<House> findByStatus(Boolean status, Pageable pageable);

	List<House> findByHouseTypes_Id(Long id);

	@Query(nativeQuery = true, value = "SELECT * FROM House WHERE status = 1 ORDER BY view DESC Limit 0, 4")
	List<House> findTop5ThanOrderByViewDesc();
}
