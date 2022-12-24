package com.rentforhouse.payload.request;

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
	
    private Double area;
	
    private String description;

    private Float price;
    
    private Integer roomNumber;
    
	/* private TypeHouse codeHouseType; */
    
    private Integer toilet;
    
	/* Tầng */
    private Integer floor;
    
}
