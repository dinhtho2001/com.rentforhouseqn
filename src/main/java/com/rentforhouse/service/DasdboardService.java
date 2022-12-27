package com.rentforhouse.service;

import org.springframework.http.ResponseEntity;

public interface DasdboardService {

	ResponseEntity<?> interactiveByYear(int year);

}
