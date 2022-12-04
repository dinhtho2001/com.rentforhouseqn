package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.payload.request.HouseTypeRequest;

public interface IHouseTypeService{
	List<HouseTypeDto> findAll();
	HouseTypeDto findById(Long id);
	HouseTypeDto save(HouseTypeRequest houseTypeRequest);
	Boolean deleteById(Long id);
}
