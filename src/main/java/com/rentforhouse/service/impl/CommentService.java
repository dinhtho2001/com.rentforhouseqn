package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentforhouse.converter.CommentConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.Comment;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.CommentRequest;
import com.rentforhouse.payload.response.CommentResponse;
import com.rentforhouse.repository.ICommentRepository;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.ICommentService;
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
	FilesStorageService storageService;

	@Override
	@Transactional
	public CommentDto save(CommentRequest request) {
		House house = houseRepository.findById(request.getHouseId()).orElse(new House());
		User user = userRepository.findById(request.getUserId()).orElse(new User());
		if (request.getId() != null) {
			Comment comment = commentRepository.findById(request.getId()).orElse(new Comment());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userRepository.findById(userDetailsImpl.getId()).map(User::getId).get();
			if (comment.getUserId() != userId) {
				throw new MyFileNotFoundException("Bạn không có quyền chỉnh sửa comment!");
			} else {
				Comment commentNew = new Comment();
				commentNew.setContent(request.getContent());
				commentNew.setId(request.getId());
				commentNew.setUserId(userId);
				commentNew.setHouse(house);
				Comment result = commentRepository.save(commentNew);
				if (result.getId() != null) {
					CommentDto commentDto = commentConverter.convertToDto(result);
					commentDto.setUser(userConverter.convertToDto(user));
					return commentDto;
				}
			}
		} else if (house.getId() != null && user.getId() != null) {
			Comment comment = new Comment();
			CommentDto commentDto = new CommentDto();
			commentDto.setUserId(user.getId());
			commentDto.setContent(request.getContent());
			comment = commentConverter.convertToEntity(commentDto);
			comment.setHouse(house);
			Comment result = commentRepository.save(comment);
			if (result.getId() != null) {
				commentDto.setId(result.getId());
				commentDto.setCreatedBy(result.getCreatedBy());
				commentDto.setCreatedDate(result.getCreatedDate());
				commentDto.setModifiedBy(result.getModifiedBy());
				commentDto.setModifiedDate(result.getModifiedDate());
				commentDto.setUser(userConverter.convertToDto(user));
				return commentDto;
			}
		}
		return new CommentDto();
	}

	@Override
	@Transactional
	public Boolean delete(Long id) {
		try {
			commentRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public CommentResponse findAllByHouse(Long id) {		
		try {
			CommentResponse response = new CommentResponse();
			CommentDto commentDto;
			List<Comment> comments = commentRepository.findAllByHouse_id(id);
			List<CommentDto> commentDtos = new ArrayList<>();
			for (Comment item : comments) {
				commentDto = commentConverter.convertToDto(item);
				UserDto userDto = userConverter.convertToDto(userRepository.findById(item.getUserId()).orElse(new User()));
				userDto.setImage(storageService.getUrlImage(userDto.getImage()));
				commentDto.setUser(userDto);
				commentDtos.add(commentDto);
			}
			response.setComment(commentDtos);
			response.setHouseId(id);
			return response;
		} catch (Exception e) {
			System.out.println(e);
			return new CommentResponse();
		}
	}

}
