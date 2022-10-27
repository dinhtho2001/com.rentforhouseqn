package com.rentforhouse.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.rentforhouse.entity.House;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long Id;
	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String phone;

	private String email;

	private Boolean status;
	
	//private List<House> houses = new ArrayList<>();

	
}
