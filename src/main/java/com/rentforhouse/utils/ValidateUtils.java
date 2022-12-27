package com.rentforhouse.utils;

import com.rentforhouse.common.ErrorParam;
import com.rentforhouse.common.SysError;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.SignupRequest;

public class ValidateUtils {
	
	public static boolean checkNullAndEmpty(String value) {
		if(value != null && value != "") {
			return false;
		}
		return true;
		
	}
	
	public static SysError validateUser(SignupRequest request) { 
		if(checkNullAndEmpty(request.getUserName())) {
			return new SysError("empty-username", new ErrorParam("username"));
		}
		else if(checkNullAndEmpty(request.getEmail())){
			return new SysError("empty-email", new ErrorParam("email"));
		}
		/*
		 * else if(checkNullAndEmpty(request.getPhone())) { return new
		 * SysError("empty-phone", new ErrorParam("phone")); }
		 */
		else if(checkNullAndEmpty(request.getPassword())) {
			return new SysError("empty-password", new ErrorParam("password"));
		}
		else if(checkNullAndEmpty(request.getLastName())){
			return new SysError("empty-lastname", new ErrorParam("lastname"));
		}
		else if(checkNullAndEmpty(request.getFirstName())) {
			return new SysError("empty-firstname", new ErrorParam("firstname"));
		}
		return new SysError();
	}
	
	public static void validateHouse(HouseRequest request) throws MyFileNotFoundException{
		if(checkNullAndEmpty(request.getName())) {
			throw new MyFileNotFoundException("Name is require!");
		}
		/*
		 * if(houseSaveRequest.getTypeIds() == null){ throw new
		 * MyFileNotFoundException("Type is require!"); }
		 */
		if(checkNullAndEmpty(request.getAddress())) {
			throw new MyFileNotFoundException("Address is require!");
		}
	}
}
