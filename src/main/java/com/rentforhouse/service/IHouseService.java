package com.rentforhouse.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.HouseSaveRequest;

public interface IHouseService {

	List<HouseDto> findHouse(HouseRequest houseRequest ,Pageable pageable);
	HouseDto saveHouse(HouseSaveRequest houseSaveRequest);
	HouseDto findById(Long id);
}
