package com.rentforhouse.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IUserService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "userAPIOfWeb")
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private IUserService userService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> findUserById(@PathVariable("id") Long id) {
		UserDto userDto = userService.findbyId(id);

		if (userDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), userDto, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> findUsers(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
		DataGetResponse dataGetResponse = new DataGetResponse();
		dataGetResponse = userService.findAll(page, limit);
		if (dataGetResponse.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), dataGetResponse, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
		if (userService.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
					new MessageResponse("successful delete"), HttpStatus.OK.name()));		
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("can-not-delete", new ErrorParam())));
		}
		
	}

	@PutMapping()
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateUser(@ModelAttribute UserRequest request, MultipartFile image) {
		UserDto userDto = userService.save(request, image);
		if (userDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), userDto, HttpStatus.OK.name()));
		} else if (userDto.getEmail() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-email", new ErrorParam(Param.email.name()))));
		} else if (userDto.getPhone() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-phone", new ErrorParam(Param.phone.name()))));
		} else if (userDto.getUserName() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-username", new ErrorParam(Param.username.name()))));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@PostMapping()
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> save(@ModelAttribute UserRequest request, MultipartFile image) {
		UserDto userDto = userService.save(request, image);
		if (userDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), userDto, HttpStatus.OK.name()));
		} else if (userDto.getEmail() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-email", new ErrorParam(Param.email.name()))));
		} else if (userDto.getPhone() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-phone", new ErrorParam(Param.phone.name()))));
		} else if (userDto.getUserName() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
					new SysError("exist-username", new ErrorParam(Param.username.name()))));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
}
