package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.entity.Role;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.RoleRequest;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService{
	@Autowired
	private IRoleRepository roleRepository;
	@Autowired
	private RoleConverter roleConverter;
	@Autowired
	private IUserRepository userRepository;

	@Override
	public List<RoleDto> findAll() {
		List<RoleDto> roleDtos = new ArrayList<>();
		List<Role> roles = roleRepository.findAll();
		roles.forEach(role ->{
			roleDtos.add(roleConverter.convertToDto(role));
		});
		return roleDtos;
	}

	@Override
	public RoleDto findById(Long id) {
		RoleDto roleDto = new RoleDto();
		if(!roleRepository.existsById(id)) {
			return new RoleDto();
		}
		roleDto = roleConverter.convertToDto(roleRepository.findById(id).get());
		return roleDto;
	}

	@Override
	@Transactional
	public RoleDto save(RoleRequest roleRequest) {
		if(roleRequest.getId() != null && !roleRepository.existsById(roleRequest.getId())) {
			return new RoleDto();
		}
		Role role = roleRepository.save(roleConverter.convertToEntity(roleRequest));
		return roleConverter.convertToDto(role);
	}

	@Override
	@Transactional
	public Boolean deleteById(Long id) {
		if(!roleRepository.existsById(id)) {
			return false;
		}	
		List<User> users = userRepository.findByRoles_Id(id); 
		Role role = roleRepository.findById(id).get();
		users.forEach(user ->{
			role.remove(user);
		}); 
		roleRepository.save(role);
		roleRepository.deleteById(id);
		return true;
	}
	

}
