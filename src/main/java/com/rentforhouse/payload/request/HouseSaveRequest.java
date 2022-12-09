package com.rentforhouse.payload.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.TypeHouse;
import com.rentforhouse.entity.House;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseSaveRequest {
	
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private String name;
	
    private String address;
	
    private String area;
	
    private String description;

    private Float price;
    
    private Integer roomNumber;
    
    private List<TypeHouse> typeHouses;
    
    @ApiModelProperty(hidden = true)
	private Boolean status;
    
    private String toilet;
    
    private String floor;
    
    public MultipartFile image;
    
}
