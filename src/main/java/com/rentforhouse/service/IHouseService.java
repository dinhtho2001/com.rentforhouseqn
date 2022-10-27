package com.rentforhouse.service;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.response.HouseResponse;

public interface IHouseService {
	HouseResponse findHouse(HouseRequest houseRequest);
	HouseDto saveHouse(HouseSaveRequest houseSaveRequest);
	HouseDto findById(Long id);
	Boolean delete(Long id);
	Object findAllByUserId(Long id, int page, int limit);
}
