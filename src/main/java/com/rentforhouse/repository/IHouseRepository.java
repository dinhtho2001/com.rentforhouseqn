package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.House;

public interface IHouseRepository extends JpaRepository<House, Long>{
	Page<House> findByNameLike(String name, Pageable pageable);
	List<House> findByNameContaining(String name);
	Page<House> findByHouseTypes_Id(Long typeId, Pageable pageable);
	Page<House> findByNameLikeAndHouseTypes_Id(String name, Long typeId,Pageable pageable);
	Page<House> findByUser_Id(Long id, Pageable pageable);
}
