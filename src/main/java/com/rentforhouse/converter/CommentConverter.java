package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rentforhouse.dto.CommentDto;
import com.rentforhouse.entity.Comment;

@Component
public class CommentConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public Comment convertToEntity(CommentDto commentDto) {		
		Comment comment = modelMapper.map(commentDto, Comment.class);
		return comment;
	}
	
	public CommentDto convertToDto(Comment comment) {
		CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
		return commentDto;
	}
}
