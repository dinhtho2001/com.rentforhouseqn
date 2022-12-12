package com.rentforhouse.service;

import org.springframework.http.ResponseEntity;

import com.rentforhouse.payload.request.SaveHouseRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;

public interface IHouseService {
	ResponseEntity<?> findHouse(SearchHouseRequest request);
	ResponseEntity<?> findById(Long id);
	ResponseEntity<?> delete(Long id);
	ResponseEntity<?> findAllByUserId(Long id, int page, int limit);
	ResponseEntity<?> findAll(int page, int limit);
	ResponseEntity<?> findHousesByStatus(Boolean status, int page, int limit);
	ResponseEntity<?> viewPlus(Long id);
	ResponseEntity<?> save(SaveHouseRequest request);
	ResponseEntity<?> findByTypeId(Long id, int page, int limit);
	ResponseEntity<?> updateStatus(Long id, Boolean status);
	ResponseEntity<?> findTop5HouseByView();
}
