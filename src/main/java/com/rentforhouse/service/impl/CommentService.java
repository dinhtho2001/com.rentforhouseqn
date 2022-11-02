package com.rentforhouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.converter.CommentConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.entity.Comment;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;
import com.rentforhouse.repository.ICommentRepository;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.ICommentService;
import com.rentforhouse.service.IDateService;

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
		if (house.getId() != null) {
			User user = new User();
			user = userRepository.findById(request.getUserId()).orElse(new User());
			Comment comment = new Comment();
			CommentResponse response = new CommentResponse();
			CommentDto commentDto = new CommentDto();
			commentDto.setUserId(request.getUserId());
			commentDto.setContent(request.getContent());
			commentDto.setCreatedBy(user.getUserName());
			commentDto.setCreatedDate(date.getDate());
			commentDto.setModifiedBy(null);
			commentDto.setCreatedBy(null);
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
