package com.rentforhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.service.IHouseService;

@RestController(value="houseAPIOfWeb")
@RequestMapping("/api/house")
public class HouseController {
	
	@Autowired
	private IHouseService houseService;

	@GetMapping
	public List<HouseDto> findHouse(@ModelAttribute HouseRequest houseRequest){	
		return houseService.findHouse(houseRequest);
	}
	
}
