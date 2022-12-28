package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.common.Param;
import com.rentforhouse.common.SysError;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IAuthService;

@CrossOrigin(origins = {"http://random-quotes-webs.s3-website-ap-southeast-1.amazonaws.com/", "http://localhost:3000/"})
@RestController(value = "AuthAPIOfWeb")
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	IAuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
		JwtResponse jwtResponse = authService.signin(loginRequest);
		if (jwtResponse.getUsername() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), jwtResponse, HttpStatus.OK.name()));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));			
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@ModelAttribute SignupRequest request) {
		return authService.signup(request);
	}
}
