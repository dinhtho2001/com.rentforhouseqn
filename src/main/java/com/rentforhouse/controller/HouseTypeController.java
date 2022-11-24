package com.rentforhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.common.Param;
import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IHouseTypeService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "houseTypeAPI")
@RequestMapping("/api/houseTypes")
public class HouseTypeController {
	@Autowired
	private IHouseTypeService houseTypeService;
	

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<HouseTypeDto> houseTypeDtos = houseTypeService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.seccess.name(),
				houseTypeDtos, HttpStatus.OK.name()));

	}

}







