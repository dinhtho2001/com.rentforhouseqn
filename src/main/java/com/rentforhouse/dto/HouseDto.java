package com.rentforhouse.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto extends AbstractDto{

    private String name;
	
    private String address;
	
    private String area;
    
    private Integer roomNumber;
	
    private String description;
    
    private Integer view;
	
	private Boolean status;

    private Float price;

    private String image;
    
    private String image2;
    
    private String image3;
    
    private String image4;
    
    private String image5;
    
    private String toilet;
    
    private String floor;
     
	/* private List<Long> typeIds; */
    
    private List<HouseTypeDto> houseTypes;

    private UserDto user;
    
    public UserDto setPassword(UserDto user) {
    	user.setPassword("");
    	return user;
    }
    
}
