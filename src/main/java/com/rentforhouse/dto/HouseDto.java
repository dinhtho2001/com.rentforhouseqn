package com.rentforhouse.dto;

import lombok.Getter;
import lombok.Setter;


public class HouseDto extends AbstractDto<HouseDto>{

    private String name;
	
    private String address;
	
    private String area;
	
    private String description;
	
    private String detailSumary;

    private Float price;

    private String image;
    
    private Long typeId;
    
    private Long userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailSumary() {
		return detailSumary;
	}

	public void setDetailSumary(String detailSumary) {
		this.detailSumary = detailSumary;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    

    
}
