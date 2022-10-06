package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;

@Component
public class UserConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public User convertToEntity(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		user.setStatus(true);
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return user;
	}
	
	public UserDto convertToDto(User user) {
		UserDto userDto = modelMapper.map(user, UserDto.class);
		return userDto;
	}
}
