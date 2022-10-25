package com.rentforhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.HouseType;

public interface IHouseTypeRepository extends JpaRepository<HouseType, Long>{
	List<HouseType> findByHouses_Id(Long id);

}
