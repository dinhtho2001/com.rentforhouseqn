package com.rentforhouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto  extends AbstractDto<CommentDto>{

	private Long id;
	
    private String content;

	private Long userId;

	private UserDto user;
	
}
