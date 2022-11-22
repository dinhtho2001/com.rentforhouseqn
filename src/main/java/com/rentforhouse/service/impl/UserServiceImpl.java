package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.response.DataGetResponse;
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

	@Override
	public DataGetResponse findAll(int page, int limit) {
		DataGetResponse dataGetResponse = new DataGetResponse();
		Pageable pageable = PageRequest.of(page - 1, limit);
		Page<User> users = userRepository.findAll(pageable);
		List<UserDto> userDtos= new ArrayList<>();
		for(User user : users) {
			UserDto userDto = new UserDto();
			userDto = userConverter.convertToDto(user);
			userDtos.add(userDto);
		}
		dataGetResponse.setTotal_page(users.getTotalPages());
		dataGetResponse.setPage(page);
		dataGetResponse.setData(userDtos);
		return dataGetResponse;
	}
	
}
