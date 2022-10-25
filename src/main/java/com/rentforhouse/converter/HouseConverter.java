package com.rentforhouse.converter;

import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;

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
		for(HouseType houseType :houseTypes) {
			Map<Long,String> type = new HashMap<Long, String>();
			type.put(houseType.getId(), houseType.getName());
			houseDto.setTypes(type);
		}
		return houseDto;
	}
	
	public House convertToEntity(HouseSaveRequest houseSaveRequest) {
		House house = modelMapper.map(houseSaveRequest, House.class);
		house.setImage(houseSaveRequest.getFile().getOriginalFilename());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetailsImpl =  (UserDetailsImpl) authentication.getPrincipal();
		User user = userRepository.findById(userDetailsImpl.getId()).get();
		house.setUser(user);
		return house;
	}
	
}
