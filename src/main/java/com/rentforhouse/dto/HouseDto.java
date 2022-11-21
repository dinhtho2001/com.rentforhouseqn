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
public class HouseDto extends AbstractDto<HouseDto>{

    private String name;
	
    private String address;
	
    private String area;
	
    private String description;
	
	private Boolean status;

    private Float price;

    private String image;
     
    private List<Long> typeIds;
    
    private List<String> typeNames;

    private UserDto user;
    
    public UserDto setPassword(UserDto user) {
    	user.setPassword("");
    	return user;
    }
    
}
