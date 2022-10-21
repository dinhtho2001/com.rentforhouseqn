package com.rentforhouse.service;

import org.springframework.data.domain.Pageable;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;

import com.rentforhouse.payload.response.HouseResponse;

public interface IHouseService {

	HouseResponse findHouse(HouseRequest houseRequest ,Pageable pageable);
	HouseDto saveHouse(HouseDto houseDto);
	HouseDto findById(Long id);
}
