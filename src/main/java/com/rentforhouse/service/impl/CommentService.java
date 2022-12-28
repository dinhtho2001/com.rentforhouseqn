package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.rentforhouse.utils.SecurityUtils;

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
		Comment commentNew = new Comment();
		if (house.getId() != null && user.getId() != null) {
			if (request.getId() != null) { /* sửa */
				Comment oldcomment = commentRepository.findById(request.getId()).orElse(new Comment());
				Long id = SecurityUtils.getPrincipal().getId();
				Long userId = userRepository.findById(id).get().getId();
				if (oldcomment.getUserId() != userId) {
					throw new MyFileNotFoundException("Bạn không có quyền chỉnh sửa comment!");
				} else {
					commentNew.setId(request.getId());
				}
			}
			commentNew.setContent(request.getContent());
			commentNew.setUserId(user.getId());
			commentNew.setHouse(house);
			Comment comment = commentRepository.save(commentNew);
			if (comment.getId() != null) {
				CommentDto commentDto = commentConverter.convertToDto(comment);
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
