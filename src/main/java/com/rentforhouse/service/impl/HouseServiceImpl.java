package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.HouseGetResponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;

@Service
public class HouseServiceImpl implements IHouseService {

	@Autowired
	private IHouseRepository houseRepository;

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
		House house = houseRepository.findById(id).orElse(new House());
		if (house.getId() != null) {
			HouseDto houseDto = houseConverter.convertToDto(house);
			houseDto.setUser(houseDto.setPassword(houseDto.getUser()));
			return houseDto;
		}
		else {
			return new HouseDto();
		}
	}

	private int totalTtem() {
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
			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());
			houseEntities = houseRepository.findAll(pageable);
			if (houseEntities.hasContent()) {
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
				}
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
	public HouseGetResponse findAllByUserId(Long userId, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		Page<House> houseEntities = null;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houseEntities = houseRepository.findByUser_Id(userId, pageable);
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
		if (request.getName() != "") {
			houses = houseRepository.findByNameContaining(request.getName());
		} else {
			houses = houseRepository.findAll();
		}
		/*
		 * List<FileInfo> fileInfos = storageService.loadAll().map(path -> { String
		 * filename = path.getFileName().toString(); String url =
		 * MvcUriComponentsBuilder .fromMethodName(HouseController.class, "getFile",
		 * path.getFileName().toString()).build().toString();
		 * 
		 * return new FileInfo(filename, url); }).collect(Collectors.toList());
		 */
		for (House house : houses) {
			HouseDto houseDto = houseConverter.convertToDto(house);
			houseDto.setUser(userConverter.convertToDto(house.getUser()));
			/*
			 * for (FileInfo fileInfo : fileInfos) { if
			 * (houseDto.getImage().equals(fileInfo.getName())) {
			 * houseDto.setImage(fileInfo.getUrl()); } }
			 */
			houseDto.setImage(storageService.getUrlImage(house.getImage()));
			houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
			houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
			houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
			houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
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
			for (House house : houses) {
				houseDto = houseConverter.convertToDto(house);
				houseDto.getUser().setPassword(null);		
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
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

	@Override
	public HouseGetResponse findByTypeId(Long id, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		Page<House> houses = null;
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houses = houseRepository.findByHouseTypes_Id(id, pageable);
			for (House house : houses) {
				houseDto = houseConverter.convertToDto(house);
				houseDto.getUser().setPassword(null);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}			
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houses.getTotalPages());
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
	@Transactional
	public boolean updateStatus(Long id, Boolean status) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if(house.getId() != null) {
				house.setStatus(status);
				houseRepository.save(house);
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);			
		}
		return false;	
	}

	@Override
	public List<HouseDto> findTop5HouseByView() {
		List<HouseDto> houseDtos = new ArrayList<>();
		try {
			List<House> houses = houseRepository.findTop5ThanOrderByViewDesc();
			for (House house : houses) {
				HouseDto houseDto = houseConverter.convertToDto(house);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDtos.add(houseDto);
			}
			return houseDtos;
		} catch (Exception e) {
			return new ArrayList<>();
		}	
	}
}
