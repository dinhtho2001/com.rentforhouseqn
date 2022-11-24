package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.common.UserRole;
import com.rentforhouse.config.jwt.JwtUtils;
import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.Role;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IAuthService;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;

@Service
public class AuthService implements IAuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private RoleConverter roleConverter;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public JwtResponse signin(LoginRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			System.out.println("Logging in with [{}]" + authentication.getPrincipal());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			String token = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			JwtResponse response = new JwtResponse();
			;
			response.setId(Integer.parseInt(userDetails.getId().toString()));
			response.setUsername(userDetails.getUsername());
			response.setRoles(roles);
			response.setAccess_token(token);
			response.setToken_type("Bearer");
			return response;

		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			return new JwtResponse();
		}

	}

	@Override
	@Transactional
	public UserDto signup(SignupRequest request) {
		UserDto dto = new UserDto();
		if (userRepository.existsByUserName(request.getUserName())) {
			dto.setUserName(request.getUserName());
			return dto;
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			dto.setEmail(request.getEmail());
			return dto;
		}
		if (userRepository.existsByPhone(request.getPhone())) {
			dto.setPhone(request.getPhone());
			return dto;
		} else {
			User user = new User();
			List<RoleDto> roleDtos = new ArrayList<>();
			List<Role> roles = roleRepository.findByName(UserRole.ROLE_USER.name());
			Role role = (roles.get(0));
			RoleDto roleDto = roleConverter.convertToDto(role);
			roleDtos.add(roleDto);
			dto.setLastName(request.getLastName());
			dto.setFirstName(request.getFirstName());
			dto.setUserName(request.getUserName());
			dto.setEmail(request.getEmail());
			dto.setPhone(request.getPhone());
			dto.setImage(null);
			dto.setPassword(passwordEncoder.encode(request.getPassword()));
			dto.setStatus(true);
			dto.setRoles(roleDtos);
			user = userRepository.save(userConverter.convertToEntity(dto));
			if (user.getId() != null) {
				return userConverter.convertToDto(user);
			} else {
				return new UserDto();
			}
		}
	}

}
