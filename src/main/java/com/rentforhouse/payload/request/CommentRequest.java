package com.rentforhouse.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

	private Long id;
	private String content;
	private Long userId;
	private Long houseId;
}
