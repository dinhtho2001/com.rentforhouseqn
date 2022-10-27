package com.rentforhouse.dto;

import com.rentforhouse.entity.House;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto  extends AbstractDto<CommentDto>{

    private String content;

	private Long userId;
	
	private Long houseId;

	
}
