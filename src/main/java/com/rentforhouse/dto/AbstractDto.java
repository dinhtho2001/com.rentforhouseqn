package com.rentforhouse.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDto implements Serializable  {
	
	@ApiModelProperty(hidden = true)
	private Long id;
	
	@ApiModelProperty(hidden = true)
	private Date createdDate;
	
	@ApiModelProperty(hidden = true)
	private String createdBy;
	
	@ApiModelProperty(hidden = true)
	private Date modifiedDate;
	
	@ApiModelProperty(hidden = true)
	private String modifiedBy;
	
	

}
