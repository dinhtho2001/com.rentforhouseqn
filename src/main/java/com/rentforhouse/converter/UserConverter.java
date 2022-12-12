package com.rentforhouse.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.Role;
import com.rentforhouse.entity.User;
import com.rentforhouse.repository.IRoleRepository;

@Component
public class UserConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IRoleRepository roleRepository;

	public User convertToEntity(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		List<Role> roles = new ArrayList<>();
		for(RoleDto roleDto : userDto.getRoles()) {
			Role role = roleRepository.findByName(roleDto.getName());
			roles.add(role);
		}
		  user.setRoles(roles);
			/*
			 * user.setStatus(true); user.setPassword(
			 * passwordEncoder.encode(userDto.getPassword()));
			 */
		 
		return user;
	}

	public UserDto convertToDto(User user) {
		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setPassword(null);
		return userDto;
	}
}
