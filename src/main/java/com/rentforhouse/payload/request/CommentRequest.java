package com.rentforhouse.payload.request;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

	/*
	 * @ApiModelProperty(hidden = true) private Long id;
	 */

	private String content;

	private Integer userId;

	private Integer houseId;
}
