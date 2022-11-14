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

import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.exception.ErrorParam;
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
	public ResponseEntity<?> save(@RequestParam("houseId") Long houseId, @RequestParam("userId") Long userId,
			@RequestParam("content") String content) {
		CommentRequest request = new CommentRequest();
		request.setHouseId(houseId);
		request.setUserId(userId);
		request.setContent(content);
		CommentDto response = commentService.save(request);
		if (response != null) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new SuccessReponse("success", response, HttpStatus.CREATED.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> edit(@RequestParam("id") Long id, @RequestParam("houseId") Long houseId, @RequestParam("userId") Long userId,
			@RequestParam("content") String content) {
		CommentRequest request = new CommentRequest();
		request.setId(id);
		request.setHouseId(houseId);
		request.setUserId(userId);
		request.setContent(content);
		CommentDto response = commentService.save(request);
		if (response.getId() != null) {
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

	@GetMapping("/house/{houseId}")
	public ResponseEntity<?> findCommentsByHouse(@PathVariable("houseId") Long id) {

		CommentResponse response = commentService.findAllByHouse(id);
		if (response.getHouseId() != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success", response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("houseId", new ErrorParam("houseId-notfound"))));
	}
}
