package com.rentforhouse.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.JwtResponse;

@Service
public interface IAuthService {
	JwtResponse signin(LoginRequest loginRequest);
	ResponseEntity<?> signup(SignupRequest signupRequest);
}
