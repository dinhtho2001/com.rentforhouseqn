package com.rentforhouse.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignupRequest {
	
	private String lastName;
	
	private String firstName;

	private String userName;

	private String password;

	/* private String phone; */

	private String email;

}
