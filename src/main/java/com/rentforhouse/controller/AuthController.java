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
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IAuthService;
import com.rentforhouse.utils.ValidateUtils;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
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
		SysError error = ValidateUtils.validateUser(request);
		if (error.getCode() != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), error));
		} else {
			UserDto userDto = authService.signup(request);
			if (userDto.getId() != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), userDto, HttpStatus.OK.name()));
			} else if (userDto.getEmail() != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-email", new ErrorParam(Param.email.name()))));
			} else if (userDto.getPhone() != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-phone", new ErrorParam(Param.phone.name()))));
			} else if (userDto.getUserName() != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-username", new ErrorParam(Param.username.name()))));
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), error));
			}
		}

	}
}
