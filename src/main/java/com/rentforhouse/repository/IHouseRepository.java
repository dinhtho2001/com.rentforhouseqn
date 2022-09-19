package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.House;

public interface IHouseRepository extends JpaRepository<House, Long>{

	List<House> findByNameLike(String name);
	
}
