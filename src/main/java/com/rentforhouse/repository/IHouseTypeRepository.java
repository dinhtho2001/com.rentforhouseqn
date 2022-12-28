package com.rentforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentforhouse.entity.HouseType;

public interface IHouseTypeRepository extends JpaRepository<HouseType, Long>{

	/* Lấy loại nhà theo code */
	HouseType findByCode(String code);
}
