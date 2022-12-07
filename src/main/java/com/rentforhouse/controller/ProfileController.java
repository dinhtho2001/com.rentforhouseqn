package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.ProfileRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IUserService;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;
import com.rentforhouse.utils.SecurityUtils;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

	@Autowired
	private IUserService userService;
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ResponseEntity<?> update(@ModelAttribute ProfileRequest request){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("Chua lam", new ErrorParam())));
	}
	
	@PostMapping("/image/{userId}/")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public ResponseEntity<?> updateImage(@PathVariable(value = "userId") Long id, @RequestParam MultipartFile file){
		if (SecurityUtils.getPrincipal().getId() != id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("You have no right to change", new ErrorParam())));
		}else {
			FileUploadResponse fileResponse =userService.updateImage(id,file);
			if (fileResponse.getUrl() != null) {
				return ResponseEntity.status(HttpStatus.OK)	.body(new SuccessReponse(Param.success.name(), fileResponse, HttpStatus.OK.name()));
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("Error", new ErrorParam())));
		}
		
	}
}
