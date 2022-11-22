package com.rentforhouse.service;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.response.DataGetResponse;

public interface IUserService {
	UserDto saveUser(UserDto userDto);
	UserDto findbyId(Long id);
	DataGetResponse findAll(int page, int limit);
}
