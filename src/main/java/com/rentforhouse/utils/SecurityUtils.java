package com.rentforhouse.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.rentforhouse.service.impl.userdetail.UserDetailsImpl;

public class SecurityUtils {

	 public static UserDetailsImpl getPrincipal() {
	        return (UserDetailsImpl) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
	    }
}
