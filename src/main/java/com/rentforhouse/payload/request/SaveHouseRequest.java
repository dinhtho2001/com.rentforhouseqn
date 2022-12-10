package com.rentforhouse.payload.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.TypeHouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaveHouseRequest {

	private Long id;

	private String name;

	private String address;

	private String area;

	private Integer roomNumber;

	private String description;

	private Integer view;

	private Boolean status;

	private Float price;

	private MultipartFile image;

	private MultipartFile image2;

	private MultipartFile image3;

	private MultipartFile image4;

	private MultipartFile image5;

	private String toilet;

	private String floor;
	
	private List<TypeHouse> typeHouses;

}
