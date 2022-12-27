package com.rentforhouse.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentforhouse.common.DataChart;
import com.rentforhouse.entity.House;
import com.rentforhouse.payload.response.LineChartResponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.service.DasdboardService;

@Service
public class DasdboardServiceImpl implements DasdboardService {

	@Autowired
	private IHouseRepository houseRepository;

	@SuppressWarnings("deprecation")
	@Override
	public ResponseEntity<?> interactiveByYear(int year) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<House> houses = houseRepository.findAll();
		List<String> nameColumns = new ArrayList<>();
		nameColumns.add("Tháng 1");
		nameColumns.add("Tháng 2");
		nameColumns.add("Tháng 3");
		nameColumns.add("Tháng 4");
		nameColumns.add("Tháng 5");
		nameColumns.add("Tháng 6");
		nameColumns.add("Tháng 7");
		nameColumns.add("Tháng 8");
		nameColumns.add("Tháng 9");
		nameColumns.add("Tháng 10");
		nameColumns.add("Tháng 11");
		nameColumns.add("Tháng 12");
		Integer value = 0;
		Integer tolTalValues = 0;
		List<DataChart> dataCharts = new ArrayList<>();
		for (int iMonth = 1; iMonth <= nameColumns.size(); iMonth++) {
			int soNgay = daysInMonth(year, iMonth);
			DataChart dataChart = new DataChart();
			for (int iDay = 0; iDay < soNgay; iDay++) {
				Date date = new Date(year - 1900, iMonth - 1, iDay + 1);
				for (House house : houses) {
					Date dateHouse;
					try {
						dateHouse = dateFormat.parse(house.getCreatedDate().toString());
						if (date.equals(dateHouse)) {
							value = value + 1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				dataChart.setColumn(nameColumns.get(iMonth - 1));
				dataChart.setValue(value);
			}
			dataCharts.add(dataChart);
			tolTalValues = tolTalValues + value;
			value = 0;

		}
		LineChartResponse response = new LineChartResponse();
		response.setNameColumns(nameColumns);
		response.setData(dataCharts);
		response.setTotalColumn(nameColumns.size());
		response.setTotalValues(tolTalValues);
		return ResponseEntity.ok(response);
	}

	private int daysInMonth(int year, int month) {
		switch (month) {
		case 1:
			return 31;
		case 3:
			return 31;
		case 5:
			return 31;
		case 7:
			return 31;
		case 8:
			return 31;
		case 10:
			return 31;
		case 12:
			return 31;

		case 4:
			return 30;
		case 6:
			return 30;
		case 9:
			return 30;
		case 11:
			return 30;

		case 2:
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				return 29;
			} else {
				return 28;
			}
		default:
			return 0;
		}
	}

	@Override
	public ResponseEntity<?> interactiveMonthByYear(int year, int month) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<House> houses = houseRepository.findAll();
		List<String> nameColumns = new ArrayList<>();
		int soNgay = daysInMonth(year, month);
		for (int i = 0; i < soNgay; i++) {
			nameColumns.add("" + (i + 1));
		}
		Integer value = 0;
		Integer tolTalValues = 0;
		
		List<DataChart> dataCharts = new ArrayList<>();
		for (int iDay = 0; iDay < soNgay; iDay++) {
			DataChart dataChart = new DataChart();
			@SuppressWarnings("deprecation")
			Date date = new Date(year - 1900, month - 1, iDay + 1);
			for (House house : houses) {
				Date dateHouse;
				try {
					dateHouse = dateFormat.parse(house.getCreatedDate().toString());
					if (date.equals(dateHouse)) {
						value = value + 1;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			dataChart.setColumn(nameColumns.get(iDay));
			dataChart.setValue(value);
			dataCharts.add(dataChart);
			tolTalValues = tolTalValues + value;
			value = 0;
		}

		LineChartResponse response = new LineChartResponse();
		response.setNameColumns(nameColumns);
		response.setData(dataCharts);
		response.setTotalColumn(nameColumns.size());
		response.setTotalValues(tolTalValues);
		return ResponseEntity.ok(response);
	}

}
