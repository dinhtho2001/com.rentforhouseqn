package com.rentforhouse.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

	private String firstName;

	private String lastName;
	
	private String phone;

	private String email;

}
