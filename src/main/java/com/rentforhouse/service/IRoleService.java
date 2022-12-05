package com.rentforhouse.service;

import java.util.List;

import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.payload.request.RoleRequest;

public interface IRoleService {
	List<RoleDto> findAll();
	RoleDto findById(Long id);
	RoleDto save(RoleRequest roleRequest);
	Boolean deleteById(Long id);
}
