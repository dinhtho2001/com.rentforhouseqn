package com.rentforhouse.service;

import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.FileUploadResponse;

public interface IUserService {
	UserDto save(UserDto dto);
	UserDto findbyId(Long id);
	DataGetResponse findAll(int page, int limit);
	Boolean delete(Long id);
	
	FileUploadResponse updateImage(Long id, MultipartFile file);
}
