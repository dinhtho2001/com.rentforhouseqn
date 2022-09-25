package com.rentforhouse.service;

import java.text.ParseException;

import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.JwtResponse;


public interface IAuthService {
	JwtResponse signin(LoginRequest loginRequest);
	User signup(SignupRequest signupRequest)throws ParseException ;
}
