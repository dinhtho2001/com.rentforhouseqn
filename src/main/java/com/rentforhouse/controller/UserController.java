package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IUserService;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value="userAPIOfWeb")
@RequestMapping("/api/users")
public class UserController {
  
	@Autowired
	private IUserService userService;
	
	@GetMapping("/{id}")
	/* @PreAuthorize("hasRole('ROLE_ADMIN')") */
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		UserDto userDto = userService.findbyId(id);
		 
		 if (userDto.getId() != null) {
				return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success",
						userDto, HttpStatus.OK.name()));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(@RequestParam(name = "page") int page, 
									 @RequestParam(name = "limit") int limit){
		DataGetResponse dataGetResponse = new DataGetResponse();
		dataGetResponse = userService.findAll(page, limit);
		if (dataGetResponse.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK)
								.body(new SuccessReponse("success", dataGetResponse, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
}
