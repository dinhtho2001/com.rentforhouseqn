package com.rentforhouse.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.ResourceDTO;

@Service
public interface IExcelService {

	ResourceDTO exportHouseToExcel();

	ResponseEntity<?> importHouses(MultipartFile file);
}
