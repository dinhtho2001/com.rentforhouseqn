package com.rentforhouse.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.FileUploadResponse;

public interface IUserService {
	ResponseEntity<?> save(UserRequest request, MultipartFile image);
	UserDto findbyId(Long id);
	DataGetResponse findAll(int page, int limit);
	ResponseEntity<?> delete(Long id);
	
	FileUploadResponse updateImage(Long id, MultipartFile file);
	ResponseEntity<?> updateRoles(Long id);
	ResponseEntity<?> search(String content, int limit, int page);
}
