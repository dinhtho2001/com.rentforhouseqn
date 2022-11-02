package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.rentforhouse.controller.HouseController;
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.HouseResponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;
import com.rentforhouse.service.IUserService;
import com.rentforhouse.utils.ValidateUtils;

@Service
public class HouseServiceImpl implements IHouseService {

	@Autowired
	private IHouseRepository houseRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private HouseConverter houseConverter;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	FilesStorageService storageService;

	@Override
	@Transactional
	public HouseDto saveHouse(HouseSaveRequest houseSaveRequest) {
		House house = new House();
		house = houseConverter.convertToEntity(houseSaveRequest);
		return houseConverter.convertToDto(houseRepository.save(house));
	}

	@Override
	public HouseDto findById(Long id) {
		House house = houseRepository.findById(id)
				.orElseThrow(() -> new MyFileNotFoundException("Id : " + id + " không tồn tại"));
		HouseDto houseDto = houseConverter.convertToDto(house);
		houseDto.setUser(houseDto.setPassword(houseDto.getUser()));
		return houseDto;
	}

	public int totalTtem() {
		try {
			return (int) houseRepository.count();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Boolean delete(Long id) {
		try {
			houseRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public HouseResponse findAllByUserId(Long id, int page, int limit) {
		HouseResponse houseResponse = new HouseResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		Page<House> houseEntities = null;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houseEntities = houseRepository.findByUser_Id(id, pageable);
			for (House item : houseEntities) {
				houseDto = houseConverter.convertToDto(item);
				houseDto.setUser(null);
				houseDtos.add(houseDto);
			}
			houseResponse.setHouses(houseDtos);
			houseResponse.setPage(page);
			houseResponse.setTotal_page(houseEntities.getTotalPages());
		} catch (Exception e) {
			return new HouseResponse();
		}
		return houseResponse;
	}

	@Override
	public List<HouseDto> findHouse(SearchHouseRequest request) {
		List<HouseDto> houseDtos = new ArrayList<>();
		List<House> houses = new ArrayList<>();
		User user;
		UserDto userDto;
		HouseDto houseDto;

		if (request.getName() != "") {
			houses = houseRepository.findByNameContaining(request.getName());
		}else {
			houses = null;
		}
		/*
		 * List<FileInfo> fileInfos = storageService.loadAll().map(path -> { String
		 * filename = path.getFileName().toString(); String url =
		 * MvcUriComponentsBuilder .fromMethodName(HouseController.class, "getFile",
		 * path.getFileName().toString()).build().toString();
		 * 
		 * return new FileInfo(filename, url); }).collect(Collectors.toList());
		 */

		for (House item : houses) {
			houseDto = houseConverter.convertToDto(item);
			user = item.getUser();
			userDto = userConverter.convertToDto(user);
			houseDto.setUser(userDto);
			/*
			 * for (FileInfo fileInfo : fileInfos) { if
			 * (houseDto.getImage().equals(fileInfo.getName())) {
			 * houseDto.setImage(fileInfo.getUrl()); } }
			 */

			houseDtos.add(houseDto);
		}
		return houseDtos;

	}

}
