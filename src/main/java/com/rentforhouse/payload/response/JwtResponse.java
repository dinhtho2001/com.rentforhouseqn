package com.rentforhouse.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

	private Integer id;
	private String username;
	private Object roles;
	private String access_token;
	private String token_type;
	
	
}
