package com.rentforhouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.ICommentService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "CommentAPIOfWeb")
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private ICommentService commentService;
	 
	@PostMapping()
	private ResponseEntity<?> save(@ModelAttribute CommentRequest request){
		CommentResponse response = commentService.save(request);
		if (response.getComment() != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessReponse("success",
					response, HttpStatus.CREATED.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
}
