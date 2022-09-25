package com.rentforhouse.payload.response;

import com.rentforhouse.exception.SysError;


public class ErrorResponse {

	private String message;
	private SysError sysError;
	
	public ErrorResponse(String message, SysError sysError) {
		super();
		this.message = message;
		this.sysError = sysError;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public SysError getSysError() {
		return sysError;
	}
	public void setSysError(SysError sysError) {
		this.sysError = sysError;
	}
}
