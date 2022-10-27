package com.rentforhouse.dto;

import java.util.ArrayList;
import java.util.List;

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

	private Long id;
	private String firstName;

	private String lastName;

	private String userName;

	private String phone;

	private String email;
	
	private String password;
	
	private List<House> houses = new ArrayList<>();

	
}
