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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.common.SysError;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IUserService;

@CrossOrigin(origins = {"http://random-quotes-webs.s3-website-ap-southeast-1.amazonaws.com/", "http://localhost:3000/"})
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
		return userService.delete(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @ModelAttribute UserRequest request,
			@RequestParam(required = false, name = "image") MultipartFile image) {
		request.setId(id.longValue());
		return userService.save(request, image);
	}

	@PostMapping()
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> save(@ModelAttribute UserRequest request,
			@RequestParam(required = false, name = "image") MultipartFile image) {
		return userService.save(request, image);
	}
	
	@PutMapping("/update-role/{id}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateRoles(@PathVariable("id") Long id,@RequestParam List<UserRole> roles) {
		return userService.updateRoles(id,roles);
	}
	
	@PutMapping("/update-status/{id}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,@RequestParam Boolean status) {
		return userService.updateStatus(id,status);
	}

	@PostMapping("/search/")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> searchAll(
			@RequestParam(required = false, name = "content") String content,
			@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
		return userService.search(content, limit, page);
	}
	@PostMapping("/search-status_user/")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> findUsersByStatus(@RequestParam Boolean status)
			 {
		return userService.findUsersByStatus(status);
	}
	
}
