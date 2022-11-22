package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.HouseGetResponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;

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
	public HouseDto save(HouseSaveRequest request) {
		if (request.getId() != null) {
			House house = new House();
			house = houseConverter.convertToEntity(request);
			return houseConverter.convertToDto(houseRepository.save(house));
		}
		else {
			House house = new House();
			house = houseConverter.convertToEntity(request);
			house.setView(0);
			house.setStatus(false);
			return houseConverter.convertToDto(houseRepository.save(house));
		}
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
	public HouseGetResponse findAll(int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		Page<House> houseEntities = null;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houseEntities = houseRepository.findAll(pageable);
			for (House item : houseEntities) {
				houseDto = houseConverter.convertToDto(item);
				houseDto.getUser().setPassword(null);
				//houseDto.setUser(null);
				houseDtos.add(houseDto);
			}			
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houseEntities.getTotalPages());
				return response;
			}
			else {
				return new HouseGetResponse();
			}
			
		} catch (Exception e) {
			return new HouseGetResponse();
		}
	}

	@Override
	public HouseGetResponse findAllByUserId(Long id, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
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
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houseEntities.getTotalPages());
				return response;
			}
			else {
				return new HouseGetResponse();
			}
		} catch (Exception e) {
			return new HouseGetResponse();
		}
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
		} else {
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

	@Override
	@Transactional
	public Boolean viewPlus(Long id) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if(house.getId() != null) {
				house.setView(house.getView()+1);
				houseRepository.save(house);
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);			
		}
		return false;				
	}

	@Override
	public HouseGetResponse findHousesByStatus(Boolean status, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		Page<House> houses = null;
		HouseDto houseDto = new HouseDto();
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houses = houseRepository.findByStatus(status, pageable);
			for (House item : houses) {
				houseDto = houseConverter.convertToDto(item);
				houseDto.getUser().setPassword(null);			
				houseDtos.add(houseDto);
			}			
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page( houses.getTotalPages());
				return response;
			}
			else {
				return new HouseGetResponse();
			}
		} catch (Exception e) {
			return new HouseGetResponse();
		}	
	}
}
