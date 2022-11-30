package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rentforhouse.common.Delete;
import com.rentforhouse.common.Param;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.Role;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.SignupRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

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
	@Transactional
	public UserDto save(UserDto dto) {
		UserDto userDto = new UserDto();
		if (userRepository.existsByUserName(dto.getUserName())) {
			userDto.setUserName(dto.getUserName());
			return userDto;
		}
		if (userRepository.existsByEmail(dto.getEmail())) {
			userDto.setEmail(dto.getEmail());
			return userDto;
		}
		if (userRepository.existsByPhone(dto.getPhone())) {
			userDto.setPhone(dto.getPhone());
			return userDto;
		} else {
			if (dto.getId() != null) {
				userDto.setId(dto.getId());
			}
			User user = new User();
			userDto.setLastName(dto.getLastName());
			userDto.setFirstName(dto.getFirstName());
			userDto.setUserName(dto.getUserName());
			userDto.setEmail(dto.getEmail());
			userDto.setPhone(dto.getPhone());
			userDto.setImage(dto.getImage());
			userDto.setPassword(passwordEncoder.encode(dto.getPassword()));
			userDto.setStatus(dto.getStatus());
			userDto.setRoles(dto.getRoles());
			user = userConverter.convertToEntity(userDto);
			if (userRepository.save(user).getId() != null) {
				return userConverter.convertToDto(userRepository.save(user));
			} else {
				return new UserDto();
			}
		}
	}

	@Override
	public UserDto findbyId(Long id) {
		User user = userRepository.findById(id).get();
		UserDto userDto = userConverter.convertToDto(user);
		userDto.setPassword(null);
		return userDto;
	}

	@Override
	public DataGetResponse findAll(int page, int limit) {
		DataGetResponse dataGetResponse = new DataGetResponse();
		Pageable pageable = PageRequest.of(page - 1, limit);
		Page<User> users = userRepository.findAll(pageable);
		List<UserDto> userDtos = new ArrayList<>();
		for (User user : users) {
			UserDto userDto = new UserDto();
			userDto = userConverter.convertToDto(user);
			userDtos.add(userDto);
		}

		if (userDtos.get(0) != null) {
			dataGetResponse.setTotal_page(users.getTotalPages());
			dataGetResponse.setPage(page);
			dataGetResponse.setData(userDtos);
			return dataGetResponse;
		} else {
			return new DataGetResponse();
		}
	}

	@Override
	@Transactional
	public Boolean delete(Long id) {
		try {
			User user = userRepository.findById(id).orElse(new User());
			if (user.getId() != null) {
				@SuppressWarnings("unchecked")
				Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder
						.getContext().getAuthentication().getAuthorities();
				List<String> roles = new ArrayList<>();
				for (GrantedAuthority authority : authorities) {
					roles.add(authority.getAuthority());
					//
					System.out.println("authority Role: " + authority.getAuthority());
				}		
				for (String strRole : roles) {
					if (strRole.equals(UserRole.ROLE_ADMIN.name())) {
						userRepository.deleteById(id);
						return true;
					}else {
						switch (strRole) {
						case "ROLE_ADMIN":
							System.out.println("đã vào Role ADMIN");			
							return false;
						case "ROLE_STAFF":
							System.out.println("đã vào ROLE_STAFF");			
							return false;

						case "ROLE_USER":
							System.out.println("đã vào ROLE_USER");
							userRepository.deleteById(id);
							return true;

						default:
							return false;
						}
					}
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
