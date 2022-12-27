package com.rentforhouse.payload.response;

import com.rentforhouse.common.SysError;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String message;
	private SysError sysError;
	
	
}
