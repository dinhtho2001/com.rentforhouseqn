package com.rentforhouse.payload.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
    
    private String image;
    
    private Integer roomNumber;
    
    private List<Long> typeIds;
    
    @ApiModelProperty(hidden = true)
	private Boolean status;
    
	/* private MultipartFile files; */ 
    
}
