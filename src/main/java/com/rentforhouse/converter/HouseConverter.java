package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;

@Component
public class HouseConverter {

	@Autowired
	private ModelMapper modelMapper;

	public HouseDto convertToDto(House houseEntity) {
		HouseDto houseDto = modelMapper.map(houseEntity, HouseDto.class);
		return houseDto;
	}
	
	public House convertToEntity(HouseDto houseDto) {
		House house = modelMapper.map(houseDto, House.class);
		house.setImage(houseDto.getFile().getOriginalFilename());
		return house;
	}
	
}
