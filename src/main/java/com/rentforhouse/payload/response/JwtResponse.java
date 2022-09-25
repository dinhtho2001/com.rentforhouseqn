package com.rentforhouse.payload.response;

public class JwtResponse {

	private Integer id;
	private String username;
	private Object roles;
	private String access_token;
	private String token_type;
	
	public JwtResponse() {
		
	}
	
	public JwtResponse(Integer id, String username, Object roles, String access_token, String token_type) {
		super();
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.access_token = access_token;
		this.token_type = token_type;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public Object getRoles() {
		return roles;
	}
	public void setRoles(Object roles) {
		this.roles = roles;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	
	
}
