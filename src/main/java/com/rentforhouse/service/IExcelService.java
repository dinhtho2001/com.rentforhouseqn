package com.rentforhouse.service;

import org.springframework.stereotype.Service;

import com.rentforhouse.common.ResourceDTO;

@Service
public interface IExcelService {

	ResourceDTO exportHouseToExcel();
}
