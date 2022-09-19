package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;

public interface IHouseService {

	List<HouseDto> findHouse(HouseRequest houseRequest );
}
