package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseTypeConverter;
import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.payload.request.HouseTypeRequest;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.service.IHouseTypeService;

@Service
public class HouseTypeServiceImpl implements IHouseTypeService{
	
	@Autowired
	private IHouseTypeRepository houseTypeRepository;
	@Autowired
	private HouseTypeConverter houseTypeConverter;
	@Autowired
	private IHouseRepository houseRepository;

	@Override
	public List<HouseTypeDto> findAll() {
		List<HouseTypeDto> houseTypeDtos = new ArrayList<>();
		List<HouseType> houseTypes = houseTypeRepository.findAll();
		for(HouseType houseType : houseTypes) {
			houseTypeDtos.add(houseTypeConverter.convertToDto(houseType));
		}
		return houseTypeDtos;
	}

	@Override
	public HouseTypeDto findById(Long id) {
		HouseTypeDto houseTypeDto = new HouseTypeDto();
		houseTypeDto = houseTypeConverter.convertToDto(houseTypeRepository.findById(id).orElse(new HouseType()));
		if (houseTypeDto.getId() != null) {
			return houseTypeDto;
		}
		return houseTypeDto;
	}

	@Override
	@Transactional
	public HouseTypeDto save(HouseTypeRequest houseTypeRequest) {
		HouseType houseType = new HouseType();
		if(houseTypeRequest.getId()!= null && !houseTypeRepository.existsById(houseTypeRequest.getId())) {
			return new HouseTypeDto();
		}
		houseType = houseTypeRepository.save(houseTypeConverter.convertToEntity(houseTypeRequest));	
		return houseTypeConverter.convertToDto(houseType);
	}

	@Override
	@Transactional
	public Boolean deleteById(Long id) {
		if(!houseTypeRepository.existsById(id)) {
			return false;
		}
		List<House> houses = houseRepository.findByHouseTypes_Id(id);
		HouseType houseType = houseTypeRepository.findById(id).get();
		for(House house : houses) {
			houseType.remove(house);
		}
		houseTypeRepository.save(houseType);
		houseTypeRepository.deleteById(id);
		return true;
	}

}
