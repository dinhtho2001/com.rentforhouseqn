package com.rentforhouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.House;

public interface IHouseRepository extends JpaRepository<House, Long>{
	Page<House> findByNameLike(String name, Pageable pageable);
	Page<House> findByHouseTypes_Id(Long typeId, Pageable pageable);
	Page<House> findByNameLikeAndHouseTypes_Id(String name, Long typeId,Pageable pageable);
	
}
