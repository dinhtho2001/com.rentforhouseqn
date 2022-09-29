package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.service.IHouseService;
import com.rentforhouse.utils.ValidateUtils;

@Service
public class HouseServiceImpl implements IHouseService{
	
	@Autowired
	private IHouseRepository houseRepository;
	
	@Autowired
	private HouseConverter houseConverter;
	

	@Override
	public List<HouseDto> findHouse(HouseRequest houseRequest, Pageable pageable) {
		List<HouseDto> houseDtos = new ArrayList<>();
		Page<House> houseEntities = null ;	
		
		if(houseRequest.getTypeId() != null && ValidateUtils.checkNullAndEmpty(houseRequest.getName())) {
			houseEntities = houseRepository.findByHouseTypes_Id(houseRequest.getTypeId(), pageable);
		}
		else if(houseRequest.getTypeId() == null && !ValidateUtils.checkNullAndEmpty(houseRequest.getName())){
			houseEntities = houseRepository.findByNameLike("%"+houseRequest.getName()+"%",pageable);
		}
		else if(houseRequest.getTypeId() != null && !ValidateUtils.checkNullAndEmpty(houseRequest.getName())) {
			houseEntities = houseRepository.findByNameLikeAndHouseTypes_Id("%"+houseRequest.getName()+"%", houseRequest.getTypeId(), pageable);
		}
		else {
			houseEntities = houseRepository.findAll(pageable);
		}
		
		for(House houseEntity : houseEntities) {		
			HouseDto houseDto = houseConverter.convertToDto(houseEntity);
			houseDtos.add(houseDto);
		}
		
		return houseDtos;
	}


	@Override
	@Transactional
	public HouseDto saveHouse(HouseDto houseDto) {
		House house = new House();
		house = houseConverter.convertToEntity(houseDto);
		return houseConverter.convertToDto(houseRepository.save(house));
	}


	@Override
	public HouseDto findById(Long id) {
		House house = houseRepository.findById(id).get();
		HouseDto houseDto = houseConverter.convertToDto(house);
		return houseDto;
	}

}
