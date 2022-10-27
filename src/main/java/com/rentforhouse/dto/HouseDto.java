package com.rentforhouse.dto;

import java.util.List;
import java.util.Map;

import com.rentforhouse.entity.House;

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
	
    private String detailSumary;

    private Float price;

    private String image;
    
    
    private List<Long> typeIds;
    
    private Long userId;

	
}
