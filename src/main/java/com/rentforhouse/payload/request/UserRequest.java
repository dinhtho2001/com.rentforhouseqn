package com.rentforhouse.payload.request;

import java.util.List;

import com.rentforhouse.common.UserRole;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	@ApiModelProperty(hidden = true)
	private Long id;
	
	private String firstName;

	private String lastName;

	private String userName;

	private String password;

	private String phone;

	private String email;

	private Boolean status;
	
	private List<UserRole> roles;
	
}
