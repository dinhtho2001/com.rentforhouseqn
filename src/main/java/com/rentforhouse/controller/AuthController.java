package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IAuthService;
import com.rentforhouse.service.IUserService;
import com.rentforhouse.utils.ValidateUtils;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	IAuthService authService;
	@Autowired
	private IUserService userService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
		JwtResponse jwtResponse = authService.signin(loginRequest);
		if (jwtResponse.getUserName() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError())
					);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success", jwtResponse, HttpStatus.OK.name()));
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto){
		ValidateUtils.validateUser(userDto);
		return ResponseEntity.ok(userService.saveUser(userDto));
	}
}
