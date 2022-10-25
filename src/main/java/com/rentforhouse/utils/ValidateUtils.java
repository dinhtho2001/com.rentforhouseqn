package com.rentforhouse.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.rentforhouse.dto.UserDto;
import com.rentforhouse.exception.MyFileNotFoundException;
import com.rentforhouse.repository.IUserRepository;

public class ValidateUtils {

	@Autowired
	private IUserRepository userRepository;
	
	public static boolean checkNullAndEmpty(String value) {
		if(value != null && value != "") {
			return false;
		}
		return true;
		
	}
	
	public static void validateUser(UserDto userDto) throws MyFileNotFoundException{
		if(checkNullAndEmpty(userDto.getUserName())) {
			throw new MyFileNotFoundException("UserName is require!");
		}
		if(checkNullAndEmpty(userDto.getEmail())){
			throw new MyFileNotFoundException("Email is require!");
		}
		if(checkNullAndEmpty(userDto.getPhone())) {
			throw new MyFileNotFoundException("Phone is require!");
		}
	}
}
