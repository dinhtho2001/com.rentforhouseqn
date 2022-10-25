package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.rentforhouse.controller.HouseController;
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.response.HouseResponse;

import com.rentforhouse.payload.request.HouseSaveRequest;

import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;
import com.rentforhouse.utils.ValidateUtils;

@Service
public class HouseServiceImpl implements IHouseService{
	
	@Autowired
	private IHouseRepository houseRepository;
	
	@Autowired
	private HouseConverter houseConverter;
	
	@Autowired
	 FilesStorageService storageService;
	
	

	@Override
	public HouseResponse findHouse(HouseRequest houseRequest, Pageable pageable) {
		List<HouseDto> houseDtos = new ArrayList<>();
		Page<House> houseEntities = null ;
		HouseResponse houseResponse;
		
		if(houseRequest.getTypeId() != null && ValidateUtils.checkNullAndEmpty(houseRequest.getName())) {
			houseEntities = houseRepository.findByHouseTypes_Id(houseRequest.getTypeId(), pageable);
		}
		else if(houseRequest.getTypeId() == null && !ValidateUtils.checkNullAndEmpty(houseRequest.getName())){
			houseEntities = houseRepository.findByNameLike("%"+houseRequest.getName()+"%",pageable);
		}
		else if(houseRequest.getTypeId() != null && !ValidateUtils.checkNullAndEmpty(houseRequest.getName())) {
			houseEntities = houseRepository.findByNameLikeAndHouseTypes_Id("%"+houseRequest.getName()+"%", houseRequest.getTypeId(), pageable);
		}
		else {
			houseEntities = houseRepository.findAll(pageable);
		}
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
		      String filename = path.getFileName().toString();
		      String url = MvcUriComponentsBuilder
		          .fromMethodName(HouseController.class, "getFile", path.getFileName().toString()).build().toString();

		      return new FileInfo(filename,url);
		    }).collect(Collectors.toList());
		
		for(House houseEntity : houseEntities) {		
			HouseDto houseDto = houseConverter.convertToDto(houseEntity);

			for(FileInfo fileInfo : fileInfos) {
				if(houseDto.getImage().equals(fileInfo.getName()) ) {
					houseDto.setImage(fileInfo.getUrl());
				}
			}
			
			houseDtos.add(houseDto);
		}
		houseResponse = new HouseResponse();
		houseResponse.setPage(houseRequest.getPage());
		houseResponse.setTotal_page((int) Math.ceil((double) (totalTtem()) / houseRequest.getLimit()));
		houseResponse.setHouses(houseDtos);
		return houseResponse;
	}


	@Override
	@Transactional
	public HouseDto saveHouse(HouseSaveRequest houseSaveRequest) {
		House house = new House();
		house = houseConverter.convertToEntity(houseSaveRequest);
		return houseConverter.convertToDto(houseRepository.save(house));
	}


	@Override
	public HouseDto findById(Long id) {
		House house = houseRepository.findById(id).get();
		HouseDto houseDto = houseConverter.convertToDto(house);
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

}
