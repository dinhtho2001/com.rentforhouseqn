package com.rentforhouse.service;

import org.springframework.stereotype.Service;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.JwtResponse;

@Service
public interface IAuthService {
	JwtResponse signin(LoginRequest loginRequest);
	UserDto signup(SignupRequest signupRequest);
}
