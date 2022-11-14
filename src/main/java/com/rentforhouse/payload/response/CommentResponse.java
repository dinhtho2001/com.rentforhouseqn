package com.rentforhouse.payload.response;

import java.util.List;

import com.rentforhouse.dto.CommentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

	private Long houseId;
	private List<CommentDto> comment;
}
