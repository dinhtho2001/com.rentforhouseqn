package com.rentforhouse.converter;

import org.springframework.security.core.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;

@Component
public class HouseConverter {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private IUserRepository userRepository;

	public HouseDto convertToDto(House houseEntity) {
		HouseDto houseDto = modelMapper.map(houseEntity, HouseDto.class);
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
