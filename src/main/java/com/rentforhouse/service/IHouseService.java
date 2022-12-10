package com.rentforhouse.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.SaveHouseRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.HouseGetResponse;

public interface IHouseService {
	List<HouseDto> findHouse(SearchHouseRequest request);
	HouseDto findById(Long id);
	Boolean delete(Long id);
	HouseGetResponse findAllByUserId(Long id, int page, int limit);
	HouseGetResponse findAll(int page, int limit);
	HouseGetResponse findHousesByStatus(Boolean status, int page, int limit);
	Boolean viewPlus(Long id);
	ResponseEntity<?> save(SaveHouseRequest request);
	HouseGetResponse findByTypeId(Long id, int page, int limit);
	boolean updateStatus(Long id, Boolean status);
	List<HouseDto> findTop5HouseByView();
}
