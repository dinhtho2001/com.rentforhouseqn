package com.rentforhouse.service.impl;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentforhouse.config.jwt.JwtUtils;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IAuthService;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;


@Service
public class AuthService implements IAuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	IUserRepository userRepository;
	
	/*
	 * @Autowired RoleRepository roleRepository;
	 */
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public JwtResponse signin(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		JwtResponse response = new JwtResponse();;
		response.setId(Integer.parseInt(userDetails.getId().toString()));
		response.setUsername(userDetails.getUsername());
		response.setRoles(roles);
		response.setAccess_token(token);
		response.setToken_type("Bearer");
		return response;
	}

	@Override
	public User signup(SignupRequest request) throws ParseException {
		return null;
	}

}
