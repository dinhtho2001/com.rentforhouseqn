package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.HouseTypeDto;

public interface IHouseTypeService{
	List<HouseTypeDto> findAll();
}
