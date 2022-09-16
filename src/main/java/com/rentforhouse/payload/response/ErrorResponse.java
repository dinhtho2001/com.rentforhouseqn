package com.rentforhouse.payload.response;

import com.rentforhouse.exception.SysError;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private String message;
	private SysError sysError;

}
