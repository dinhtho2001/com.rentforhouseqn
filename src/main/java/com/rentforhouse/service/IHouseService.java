package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.HouseGetResponse;

public interface IHouseService {
	List<HouseDto> findHouse(SearchHouseRequest request);
	HouseDto saveHouse(HouseSaveRequest houseSaveRequest);
	HouseDto findById(Long id);
	Boolean delete(Long id);
	Object findAllByUserId(Long id, int page, int limit);
	HouseGetResponse findAll(int page, int limit);
	Boolean viewPlus(Long id);
}
