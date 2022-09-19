package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.service.IHouseService;

@Service
public class HouseServiceImpl implements IHouseService{
	
	@Autowired
	private IHouseRepository houseRepository;
	
	@Autowired
	private HouseConverter houseConverter;

	@Override
	public List<HouseDto> findHouse(HouseRequest houseRequest) {
		List<HouseDto> houseDtos = new ArrayList<>();
		List<House> houseEntities =  new ArrayList<>();
		
		if(houseRequest.getTypeId() != null) {
			
		}
		else {
			houseEntities = houseRepository.findByNameLike("%"+houseRequest.getName()+"%");
		}
		
		for(House houseEntity : houseEntities) {
		
			HouseDto houseDto = houseConverter.convertToDto(houseEntity);
			
			houseDtos.add(houseDto);
		}
		
		return houseDtos;
	}

}
