package com.rentforhouse.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.rentforhouse.common.ResourceDTO;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.service.IExcelService;

@Service
public class HouseExportExcelService implements IExcelService {

	@Autowired
	private IHouseRepository houseRepository;

	@Override
	public ResourceDTO exportHouseToExcel() {
		List<House> houses = houseRepository.findAll();
		Resource resource = prepareExcel(houses);

		return ResourceDTO.builder().resource(resource)
				.mediaType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.build();
	}

	private Resource prepareExcel(List<House> houses) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("PostHouses");

		writeHeaderLine(workbook, sheet, "STT", "Title", "Description", "Address", "Are", "Room Number", "Price",
				"Status", "View", "Image", "Image2", "Image3", "Image4", "Image5", "Create By", "Create Date",
				"Modified By", "Modified Date", "Toilet", "Floor", "User", "House Type");
		writeDataLines(workbook, sheet, houses);

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

			workbook.write(byteArrayOutputStream);
			return new ByteArrayResource(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error while generating excel.");
		}
	}

	private void writeHeaderLine(Workbook workbook, Sheet sheet, String... headers) {

		Row headerRow = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontName("Arial");

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);

		int columnNo = 0;
		for (String header : headers) {
			Cell headerCell = headerRow.createCell(columnNo++);
			headerCell.setCellValue(header);
			headerCell.setCellStyle(cellStyle);
		}

	}

	private void createCell(Sheet sheet, Row row, int columnCount, Object value, CellStyle cellStyle) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Long || value instanceof Double) {
			cell.setCellValue((Double) value);
		} else if (value instanceof Float) {
			cell.setCellValue((Float) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Date) {
			cell.setCellValue(((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		} else {
			cell.setCellValue((String) value);
		}

		cell.setCellStyle(cellStyle);
	}

	private void writeDataLines(Workbook workbook, Sheet sheet, List<House> houses) {

		Integer stt = 1;
		int rowNo = 1;
		Font font = workbook.createFont();
		font.setFontName("Arial");

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);

		for (House house : houses) {
			Integer columnCount = 0;
			Row row = sheet.createRow(rowNo);
			createCell(sheet, row, columnCount++, stt++, cellStyle);
			createCell(sheet, row, columnCount++, house.getName(), cellStyle);
			createCell(sheet, row, columnCount++, house.getDescription(), cellStyle);
			createCell(sheet, row, columnCount++, house.getAddress(), cellStyle);
			createCell(sheet, row, columnCount++, house.getArea(), cellStyle);
			createCell(sheet, row, columnCount++, house.getRoomNumber(), cellStyle);
			createCell(sheet, row, columnCount++, house.getPrice(), cellStyle);
			createCell(sheet, row, columnCount++, house.getStatus(), cellStyle);
			createCell(sheet, row, columnCount++, house.getView(), cellStyle);
			createCell(sheet, row, columnCount++, house.getImage(), cellStyle);
			createCell(sheet, row, columnCount++, house.getImage2(), cellStyle);
			createCell(sheet, row, columnCount++, house.getImage3(), cellStyle);
			createCell(sheet, row, columnCount++, house.getImage4(), cellStyle);
			createCell(sheet, row, columnCount++, house.getImage5(), cellStyle);
			createCell(sheet, row, columnCount++, house.getCreatedBy(), cellStyle);
			createCell(sheet, row, columnCount++, house.getCreatedDate(), cellStyle);
			createCell(sheet, row, columnCount++, house.getModifiedBy(), cellStyle);
			createCell(sheet, row, columnCount++, house.getModifiedDate(), cellStyle);
			createCell(sheet, row, columnCount++, house.getToilet(), cellStyle);
			createCell(sheet, row, columnCount++, house.getFloor(), cellStyle);
			createCell(sheet, row, columnCount++, house.getUser().getUserName(), cellStyle);
			List<String> lstHouseType = new ArrayList<>();
			for (HouseType item : house.getHouseTypes()) {
				lstHouseType.add(item.getName());
			}
			createCell(sheet, row, columnCount++, lstHouseType.toString(), cellStyle);
			rowNo++;
		}
	}
}
