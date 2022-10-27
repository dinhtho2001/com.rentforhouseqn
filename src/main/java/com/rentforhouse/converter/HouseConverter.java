package com.rentforhouse.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;
import com.rentforhouse.utils.ValidateUtils;

@Component
public class HouseConverter {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IHouseTypeRepository houseTypeRepository;
	

	public HouseDto convertToDto(House houseEntity) {
		HouseDto houseDto = modelMapper.map(houseEntity, HouseDto.class);
		List<HouseType> houseTypes = houseTypeRepository.findByHouses_Id(houseEntity.getId());
		List<Long> typeIds = new ArrayList<>();
		List<String> typeNames = new ArrayList<>();
		for(HouseType houseType :houseTypes) {
			typeIds.add(houseType.getId());
			typeNames.add(houseType.getName());
		}
		houseDto.setTypeIds(typeIds);
		houseDto.setTypeNames(typeNames);
		return houseDto;
	}
	
	public House convertToEntity(HouseSaveRequest houseSaveRequest){
		House house = modelMapper.map(houseSaveRequest, House.class);
		List<HouseType> houseTypes = new ArrayList<>();
		if(houseSaveRequest.getId() == null) {
			house.setImage(houseSaveRequest.getFiles().getOriginalFilename());
		}		

		for(Long item : houseSaveRequest.getTypeIds()) {
			HouseType houseType = new HouseType();
			houseType = houseTypeRepository.findById(item).orElseThrow((() -> new MyFileNotFoundException("Type Id : "+item+" không tồn tại")));
			houseTypes.add(houseType);
		}
		house.setHouseTypes(houseTypes);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetailsImpl =  (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepository.findById(userDetailsImpl.getId()).get();
		house.setUser(user);
		return house;
	}
	
	public House convertToEntity(HouseSaveRequest houseSaveRequest, House house) {
		house = modelMapper.map(houseSaveRequest, House.class);
		return house;
	}
	
}
