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
public class UserDto {

	private Long Id;
	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String phone;

	private String email;

	private Boolean status;
	
	private String image;
	//private List<House> houses = new ArrayList<>();
	
	private List<RoleDto> roles;

	
}
