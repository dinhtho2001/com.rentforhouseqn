package com.rentforhouse.controller;

import javax.validation.Valid;

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

import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.ICommentService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "CommentAPIOfWeb")
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private ICommentService commentService;

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	private ResponseEntity<?> save(@ModelAttribute CommentRequest request) {
		/* CommentResponse response = */commentService.save(request);
		
		/*
		 * if (response.getComment() != null) { return
		 * ResponseEntity.status(HttpStatus.CREATED).body(new SuccessReponse("success",
		 * response, HttpStatus.CREATED.name())); }
		 * 
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(new
		 * ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
		 */
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body("okok");
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> edit(@ModelAttribute CommentRequest request, @RequestParam(value = "id") Long id) {
		// request.setId(id);
		CommentResponse response = commentService.save(request);
		if (response.getComment() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		if (commentService.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new SuccessReponse("success", new MessageResponse("successful delete"), HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping("/house/{id}")
	public ResponseEntity<?> findCommentsByHouse(@PathVariable("id") Long id) {

		if (true) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success", null, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
}
