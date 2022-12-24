package com.rentforhouse.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.UserRole;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.ProfileRequest;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.FileUploadResponse;

public interface IUserService {
	ResponseEntity<?> save(UserRequest request, MultipartFile image);
	UserDto findbyId(Long id);
	DataGetResponse findAll(int page, int limit);
	ResponseEntity<?> delete(Long id);
	ResponseEntity<?> updateImage(MultipartFile file);
	ResponseEntity<?> updateRoles(Long id, List<UserRole> roles);
	ResponseEntity<?> search(String content, int limit, int page);
	ResponseEntity<?> updateProfile(ProfileRequest request);
	ResponseEntity<?> updateStatus(Long id, Boolean status);
	ResponseEntity<?> findUsersByStatus(Boolean status);
}
