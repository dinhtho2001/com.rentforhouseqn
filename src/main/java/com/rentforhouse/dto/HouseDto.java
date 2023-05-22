package com.rentforhouse.dto;

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
	
    private Double area;
    
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
    
    private Integer toilet;
    
    private Integer floor;
    
    private Boolean hide;
    
    private HouseTypeDto houseType; 

    private UserDto user;
    
    public UserDto setPassword(UserDto user) {
    	user.setPassword("");
    	return user;
    }
    
}
