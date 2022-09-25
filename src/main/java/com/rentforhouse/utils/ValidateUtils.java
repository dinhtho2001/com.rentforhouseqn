package com.rentforhouse.utils;

public class ValidateUtils {

	public static boolean checkNullAndEmpty(String value) {
		if(value != null && value != "") {
			return false;
		}
		return true;
		
	}
}
