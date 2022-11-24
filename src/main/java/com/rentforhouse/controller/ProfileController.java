package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.ProfileRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.service.IUserService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

	@Autowired
	private IUserService userService;
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ResponseEntity<?> update(@ModelAttribute ProfileRequest request){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("Chua lam", new ErrorParam())));
	}
	
	@PostMapping("/image")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ResponseEntity<?> updateImage(@RequestParam MultipartFile file){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("Chua lam", new ErrorParam())));
	}
}
