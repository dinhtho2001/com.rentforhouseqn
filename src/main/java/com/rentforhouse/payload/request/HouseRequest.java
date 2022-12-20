package com.rentforhouse.payload.request;

import java.util.List;

import com.rentforhouse.common.TypeHouse;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseRequest {
	
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private String name;
	
    private String address;
	
    private String area;
	
    private String description;

    private Float price;
    
    private Integer roomNumber;
    
    private List<TypeHouse> typeHouses;
    
	/*
	 * @ApiModelProperty(hidden = true) private Boolean status;
	 */
    
    private String toilet;
    
	/* Tầng */
    private String floor;
    
}
