package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.common.ErrorParam;
import com.rentforhouse.common.Param;
import com.rentforhouse.common.SysError;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.LoginRequest;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.JwtResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IAuthService;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;
import com.rentforhouse.utils.JwtUtils;

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
	public ResponseEntity<?>  signup(SignupRequest request) {
		UserDto dto = new UserDto();
		if (userRepository.existsByUserName(request.getUserName())) {
			dto.setUserName(request.getUserName());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-username", new ErrorParam(Param.username.name()))));
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			dto.setEmail(request.getEmail());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-email", new ErrorParam(Param.email.name()))));
		}
		/*
		 * if (userRepository.existsByPhone(request.getPhone())) {
		 * dto.setPhone(request.getPhone()); return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-phone", new ErrorParam(Param.phone.name())))); }
		 */ else {
			List<RoleDto> roleDtos = new ArrayList<>();
			roleDtos.add(roleConverter.convertToDto(roleRepository.findByName(UserRole.ROLE_USER.name())));
			dto.setLastName(request.getLastName());
			dto.setFirstName(request.getFirstName());
			dto.setUserName(request.getUserName());
			dto.setEmail(request.getEmail());
			dto.setPhone(null);
			dto.setImage(null);
			dto.setPassword(passwordEncoder.encode(request.getPassword()));
			dto.setStatus(true);
			dto.setRoles(roleDtos);
			User user = userRepository.save(userConverter.convertToEntity(dto));
			if (user.getId() != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), userConverter.convertToDto(user), HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
			}
		}
	}

}
