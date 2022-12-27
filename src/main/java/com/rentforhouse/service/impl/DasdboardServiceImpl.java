package com.rentforhouse.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentforhouse.common.DataChart;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.response.LineChartResponse;
import com.rentforhouse.payload.response.PieChartResponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.DasdboardService;

@Service
public class DasdboardServiceImpl implements DasdboardService {

	@Autowired
	private IHouseRepository houseRepository;

	@Autowired
	private IUserRepository userRepository;

	@SuppressWarnings("deprecation")
	@Override
	public ResponseEntity<?> interactiveByYear(int year) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<House> houses = houseRepository.findAll();
		List<User> users = userRepository.findAll();
		List<String> nameColumns = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			nameColumns.add("Tháng " + (i + 1));
		}
		Integer valueHouse = 0;
		Integer valueUser = 0;
		Integer tolTalValuesHouses = 0;
		Integer tolTalValuesUsers = 0;
		List<DataChart> dataChartHouses = new ArrayList<>();
		List<DataChart> dataChartUsers = new ArrayList<>();
		for (int iMonth = 1; iMonth <= nameColumns.size(); iMonth++) {
			int soNgay = daysInMonth(year, iMonth);
			DataChart dataChartHouse = new DataChart();
			DataChart dataChartUser = new DataChart();
			for (int iDay = 0; iDay < soNgay; iDay++) {
				Date date = new Date(year - 1900, iMonth - 1, iDay + 1);
				for (House house : houses) {
					Date dateHouse;
					try {
						dateHouse = dateFormat.parse(house.getCreatedDate().toString());
						if (date.equals(dateHouse)) {
							valueHouse = valueHouse + 1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				for (User user : users) {
					Date dateUser;
					try {
						dateUser = dateFormat.parse(user.getCreatedDate().toString());
						if (date.equals(dateUser)) {
							valueUser = valueUser + 1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				dataChartHouse.setColumn(nameColumns.get(iMonth - 1));
				dataChartHouse.setValue(valueHouse);
				dataChartUser.setColumn(nameColumns.get(iMonth - 1));
				dataChartUser.setValue(valueUser);
			}
			dataChartHouses.add(dataChartHouse);
			dataChartUsers.add(dataChartUser);
			tolTalValuesHouses = tolTalValuesHouses + valueHouse;
			tolTalValuesUsers = tolTalValuesUsers + valueUser;
			valueHouse = 0;
			valueUser = 0;

		}
		LineChartResponse response = new LineChartResponse();
		response.setNameColumns(nameColumns);
		response.setDataHouses(dataChartHouses);
		response.setDataUsers(dataChartUsers);
		response.setTotalDataHouses(tolTalValuesHouses);
		response.setTotalDataUsers(tolTalValuesUsers);
		response.setTotalColumn(nameColumns.size());
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
		List<User> users = userRepository.findAll();
		List<String> nameColumns = new ArrayList<>();
		int soNgay = daysInMonth(year, month);
		for (int i = 0; i < soNgay; i++) {
			nameColumns.add("" + (i + 1));
		}
		Integer valueHouse = 0;
		Integer valueUser = 0;
		Integer tolTalValuesHouses = 0;
		Integer tolTalValuesUsers = 0;

		List<DataChart> dataChartHouses = new ArrayList<>();
		List<DataChart> dataChartUsers = new ArrayList<>();
		for (int iDay = 0; iDay < soNgay; iDay++) {
			DataChart dataChartHouse = new DataChart();
			DataChart dataChartUser = new DataChart();
			@SuppressWarnings("deprecation")
			Date date = new Date(year - 1900, month - 1, iDay + 1);
			for (House house : houses) {
				Date dateHouse;
				try {
					dateHouse = dateFormat.parse(house.getCreatedDate().toString());
					if (date.equals(dateHouse)) {
						valueHouse = valueHouse + 1;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			for (User user : users) {
				Date dateUser;
				try {
					dateUser = dateFormat.parse(user.getCreatedDate().toString());
					if (date.equals(dateUser)) {
						valueUser = valueUser + 1;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			dataChartHouse.setColumn(nameColumns.get(iDay));
			dataChartHouse.setValue(valueHouse);
			dataChartHouses.add(dataChartHouse);
			dataChartUser.setColumn(nameColumns.get(iDay));
			dataChartUser.setValue(valueUser);
			dataChartUsers.add(dataChartUser);
			tolTalValuesHouses = tolTalValuesHouses + valueHouse;
			valueHouse = 0;
			tolTalValuesUsers = tolTalValuesUsers + valueUser;
			valueUser = 0;
		}

		LineChartResponse response = new LineChartResponse();
		response.setNameColumns(nameColumns);
		response.setDataHouses(dataChartHouses);
		response.setDataUsers(dataChartUsers);
		response.setTotalDataHouses(tolTalValuesHouses);
		response.setTotalDataUsers(tolTalValuesUsers);
		response.setTotalColumn(nameColumns.size());
		return ResponseEntity.ok(response);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResponseEntity<?> interactivePieChart(int year) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> nameColumns = new ArrayList<>();
		nameColumns.add("Nhà cho thuê");
		nameColumns.add("Nhà bán");
		Integer totalNhaChoThue = 0;
		Integer totalNhaBan = 0;
		List<House> houses = houseRepository.findAll();
		for (int iMonth = 1; iMonth <= 12; iMonth++) {
			int soNgay = daysInMonth(year, iMonth);
			for (int iDay = 0; iDay < soNgay; iDay++) {
				Date date = new Date(year - 1900, iMonth - 1, iDay + 1);
				for (House house : houses) {
					Date dateHouse;
					try {
						dateHouse = dateFormat.parse(house.getCreatedDate().toString());
						if (date.equals(dateHouse)) {
							String code = house.getHouseType().getCode();
							switch (code) {
							case "nha_cho_thue":
								totalNhaChoThue++;
								break;
							case "nha_ban":
								totalNhaBan++;
								break;

							default:
								break;
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

		}

		DataChart dataChartChoThue = new DataChart();
		dataChartChoThue.setColumn(nameColumns.get(0));
		dataChartChoThue.setValue(totalNhaChoThue);
		DataChart dataChartBan = new DataChart();
		dataChartBan.setColumn(nameColumns.get(1));
		dataChartBan.setValue(totalNhaBan);
		List<DataChart> datasChart = new ArrayList<>();
		datasChart.add(dataChartChoThue);
		datasChart.add(dataChartBan);
		PieChartResponse response = new PieChartResponse();
		response.setDatas(datasChart);
		response.setNameColumns(nameColumns);
		response.setTotalColumn(nameColumns.size());
		return ResponseEntity.ok(response);
	}

}
