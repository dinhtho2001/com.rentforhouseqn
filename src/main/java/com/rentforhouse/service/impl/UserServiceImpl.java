package com.rentforhouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private UserConverter userConverter;

	@Override
	public UserDto saveUser(UserDto userDto) {
		if(userRepository.existsByUserName(userDto.getUserName())) {
			
		}
		if(userRepository.existsByEmail(userDto.getEmail())){
			
		}
		if(userRepository.existsByPhone(userDto.getPhone())) {
			
		}
		
		User user = new User();
		user = userConverter.convertToEntity(userDto);
		return userConverter.convertToDto(userRepository.save(user));
	}
	
}
