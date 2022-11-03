package com.rentforhouse.payload.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

	@ApiModelProperty(hidden = true)
	private Long id;
	private String content;
	private Long userId;
	private Long houseId;
}
