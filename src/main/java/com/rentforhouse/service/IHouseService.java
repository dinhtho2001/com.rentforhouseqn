package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.request.HouseRequest;

public interface IHouseService {

	List<HouseDto> findHouse(HouseRequest houseRequest );
}
