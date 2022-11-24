package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.entity.Role;

@Component
public class RoleConverter {

	@Autowired
	private ModelMapper modelMapper;

	public Role convertToEntity(RoleDto dto) {

		Role role = modelMapper.map(dto, Role.class);
		return role;
	}

	public RoleDto convertToDto(Role role) {
		RoleDto dto = modelMapper.map(role, RoleDto.class);
		return dto;
	}
}
