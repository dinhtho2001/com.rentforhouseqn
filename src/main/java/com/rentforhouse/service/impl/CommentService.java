package com.rentforhouse.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.converter.CommentConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.entity.Comment;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.repository.ICommentRepository;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.ICommentService;
import com.rentforhouse.service.IDateService;
import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;

@Service
public class CommentService implements ICommentService {

	@Autowired
	private ICommentRepository commentRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IHouseRepository houseRepository;
	
	@Autowired
	private CommentConverter commentConverter;
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private IDateService date;

	@Override
	@Transactional
	public CommentResponse save(CommentRequest request) {		
		House house = new House();		
		house = houseRepository.findById(request.getHouseId()).orElse(new House());
		if(request.getId() != null) {
			Comment editComment = commentRepository.findById(request.getId()).orElse(new Comment());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetailsImpl =  (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userRepository.findById(userDetailsImpl.getId()).map(User :: getId).get();
			if(editComment.getUserId() != userId) {
				throw new MyFileNotFoundException("Bạn không có quyền chỉnh sửa comment!");
			}
		}
		
		if (house.getId() != null) {
			User user = new User();
			user = userRepository.findById(request.getUserId()).orElse(new User());
			Comment comment = new Comment();
			CommentResponse response = new CommentResponse();
			CommentDto commentDto = new CommentDto();
			commentDto.setId(request.getId());
			commentDto.setUserId(request.getUserId());
			commentDto.setContent(request.getContent());
			commentDto.setCreatedBy(user.getUserName());
			commentDto.setCreatedDate(date.getDate()); 
			commentDto.setModifiedBy(user.getUserName());
			commentDto.setModifiedDate(date.getDate());			 
			comment = commentConverter.convertToEntity(commentDto);
			comment.setHouse(house);
			Comment result = commentRepository.save(comment);
			if(result.getId() != null) {
				response.setHouseId(result.getHouse().getId());
				response.setComment(commentConverter.convertToDto(result));
				response.setUser(userConverter.convertToDto(user));
				return response;
			}
		}
		return new CommentResponse();
	}

}