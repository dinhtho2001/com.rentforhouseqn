package com.rentforhouse.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysError {

	private String code;
	private ErrorParam errorParams;
	

}
