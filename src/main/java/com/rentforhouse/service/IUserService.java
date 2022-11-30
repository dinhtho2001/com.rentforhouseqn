package com.rentforhouse.service;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.DataGetResponse;

public interface IUserService {
	UserDto save(UserDto dto);
	UserDto findbyId(Long id);
	DataGetResponse findAll(int page, int limit);
	String delete(Long id);
}
