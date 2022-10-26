package com.rentforhouse.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private UserConverter userConverter;

	@Override
	@Transactional
	public UserDto saveUser(UserDto userDto) throws MyFileNotFoundException{
		if(userRepository.existsByUserName(userDto.getUserName())) {
			throw new MyFileNotFoundException("Tên đăng nhập đã tồn tại!");
		}
		if(userRepository.existsByEmail(userDto.getEmail())){
			throw new MyFileNotFoundException("Email đã tồn tại!");
		}
		if(userRepository.existsByPhone(userDto.getPhone())) {
			throw new MyFileNotFoundException("Số điện thoại đã tồn tại!");
		}
		
		User user = new User();
		user = userConverter.convertToEntity(userDto);
		return userConverter.convertToDto(userRepository.save(user));
	}

	@Override
	public UserDto findbyId(Long id) {
		User user = userRepository.findById(id).get();
		UserDto userDto = userConverter.convertToDto(user);
		userDto.setPassword(null);
		return userDto;
	}
	
}
