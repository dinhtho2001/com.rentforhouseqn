package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;

@Component
public class UserConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public User convertToEntity(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		return user;
	}
}
