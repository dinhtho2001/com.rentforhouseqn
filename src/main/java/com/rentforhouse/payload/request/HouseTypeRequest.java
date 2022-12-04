package com.rentforhouse.payload.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HouseTypeRequest {
	@ApiModelProperty(hidden = true)
	private Long id;
	private String name;
}
