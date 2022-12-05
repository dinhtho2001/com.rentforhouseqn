package com.rentforhouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.common.Param;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.RoleRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IRoleService;
import com.rentforhouse.utils.ValidateUtils;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/role")
public class RoleController {
	@Autowired
	private IRoleService roleService;
	
	@GetMapping
	public ResponseEntity<?> findAll(){
		List<RoleDto> roleDtos = roleService.findAll();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),roleDtos,HttpStatus.OK.name()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		RoleDto roleDto = new RoleDto();
		roleDto = roleService.findById(id);
		if(roleDto.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
			
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),roleDto,HttpStatus.OK.name()));
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> saveRole(@ModelAttribute RoleRequest roleRequest){
		if(ValidateUtils.checkNullAndEmpty(roleRequest.getName())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("empty-rolename", new ErrorParam("rolename"))));
		}
		RoleDto roleDto = roleService.save(roleRequest);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),roleDto,HttpStatus.OK.name()));

	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> updateRole(@ModelAttribute RoleRequest roleRequest 
												,@PathVariable(value = "id") Long id){
		if(ValidateUtils.checkNullAndEmpty(roleRequest.getName())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("empty-rolename", new ErrorParam("rolename"))));
		}
		roleRequest.setId(id);
		RoleDto roleDto = roleService.save(roleRequest);
		if(roleDto.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),roleDto,HttpStatus.OK.name()));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteRoleById(@PathVariable("id") Long id) {
		if (!roleService.deleteById(id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id"))));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
				new MessageResponse("successful delete"), HttpStatus.OK.name()));
	}

}
