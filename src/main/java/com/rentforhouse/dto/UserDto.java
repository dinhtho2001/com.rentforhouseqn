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
public class UserDto extends AbstractDto<UserDto>{

	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String phone;

	private String email;

	private Boolean status;
	
	private String image;
	
	private List<RoleDto> roles;

	
}
