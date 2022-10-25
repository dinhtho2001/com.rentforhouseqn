package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.entity.HouseType;

@Component
public class HouseTypeConverter {
	@Autowired
	private ModelMapper modelMapper;

	public HouseTypeDto convertToDto(HouseType houseType) {
		HouseTypeDto houseTypeDto = modelMapper.map(houseType, HouseTypeDto.class);
		return houseTypeDto;
	}
}
