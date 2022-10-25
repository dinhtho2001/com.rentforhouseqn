package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseTypeConverter;
import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.service.IHouseTypeService;

@Service
public class HouseTypeServiceImpl implements IHouseTypeService{
	
	@Autowired
	private IHouseTypeRepository houseTypeRepository;
	@Autowired
	private HouseTypeConverter houseTypeConverter;

	@Override
	public List<HouseTypeDto> findAll() {
		List<HouseTypeDto> houseTypeDtos = new ArrayList<>();
		List<HouseType> houseTypes = houseTypeRepository.findAll();
		for(HouseType houseType : houseTypes) {
			houseTypeDtos.add(houseTypeConverter.convertToDto(houseType));
		}
		return houseTypeDtos;
	}

}
