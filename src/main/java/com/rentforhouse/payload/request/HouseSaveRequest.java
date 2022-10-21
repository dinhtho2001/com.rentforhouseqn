package com.rentforhouse.payload.request;

import org.springframework.web.multipart.MultipartFile;

public class HouseSaveRequest {
	private String name;
	
    private String address;
	
    private String area;
	
    private String description;
	
    private String detailSumary;

    private Float price;

    
    private MultipartFile file;

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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
    
    
}
