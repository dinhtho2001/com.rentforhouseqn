package com.rentforhouse.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.common.ResourceDTO;
import com.rentforhouse.entity.House;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IExcelService;
import com.rentforhouse.utils.SecurityUtils;

@Service
public class HouseExportExcelService implements IExcelService {

	@Autowired
	private IHouseRepository houseRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	FilesStorageService storageService;

	private static final Path rootPath = Paths.get("src/main/resources/assets");
	
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
			createCell(sheet, row, columnCount++, house.getHouseType().getName(), cellStyle);
			rowNo++;
		}
	}

	@Override
	public ResponseEntity<?> importHouses(MultipartFile file) {
		try {
			ResponseEntity<?> fileResponse = storageService.save(file, "");
			FileUploadResponse fileUploadResponse = (FileUploadResponse) fileResponse.getBody();
			try {
				if (saveHousesToExcel(fileUploadResponse.getFileName())) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new SuccessReponse(Param.success.name(), null, HttpStatus.OK.name()));
				} else {
					ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
							new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("error: ", new ErrorParam())));
				}
			} catch (Exception e) {
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("error: " + e, new ErrorParam())));
			}
		} catch (Exception e) {
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("error: " + e, new ErrorParam())));
		}

		return null;
	}

	@Transactional
	private Boolean saveHousesToExcel(String fileName) throws IOException {
		String strRootPath = rootPath.getParent().toString()+"/"+rootPath.getFileName();
		File file = new File(strRootPath + "/" + fileName);
		if (fileName.contains(".xlsx")) {
			FileInputStream fileInputStream = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			List<House> houses = readHousesToExcel(sheet);
			workbook.close();
			for (House house : houses) {
				houseRepository.save(house);
			}
			return true;
		} else if (fileName.contains(".xls")) {
			FileInputStream fileInputStream = new FileInputStream(file);
			Workbook workbook = new HSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			List<House> houses = readHousesToExcel(sheet);
			workbook.close();
			for (House house : houses) {
				houseRepository.save(house);
			}
			return true;
		}
		return false;
	}

	private List<House> readHousesToExcel(Sheet sheet) {
		List<House> houses = new ArrayList<>();
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			Integer columnCount = 0;
			House house = new House();
			Row row = sheet.getRow(i);
			house.setName(row.getCell(columnCount++).getStringCellValue());
			house.setDescription(row.getCell(columnCount++).getStringCellValue());
			house.setAddress(row.getCell(columnCount++).getStringCellValue());
			house.setArea(row.getCell(columnCount++).getNumericCellValue());
			house.setRoomNumber((int) row.getCell(columnCount++).getNumericCellValue());
			house.setPrice((float) row.getCell(columnCount++).getNumericCellValue());
			if (row.getCell(columnCount++).getBooleanCellValue()) {
				house.setStatus(true);
			} else {
				house.setStatus(false);
			}
			house.setView((int) row.getCell(columnCount++).getNumericCellValue());

			house.setImage(row.getCell(columnCount++).getStringCellValue());
			house.setImage2(row.getCell(columnCount++).getStringCellValue());
			house.setImage3(row.getCell(columnCount++).getStringCellValue());
			house.setImage4(row.getCell(columnCount++).getStringCellValue());
			house.setImage5(row.getCell(columnCount++).getStringCellValue());
			house.setCreatedBy(row.getCell(columnCount++).getStringCellValue());
			house.setCreatedDate(row.getCell(columnCount++).getDateCellValue());
			house.setModifiedBy(row.getCell(columnCount++).getStringCellValue());
			house.setModifiedDate(row.getCell(columnCount++).getDateCellValue());
			house.setToilet((int) row.getCell(columnCount++).getNumericCellValue());
			house.setFloor((int) row.getCell(columnCount++).getNumericCellValue());
			house.setUser(userRepository.findById(SecurityUtils.getPrincipal().getId()).get());
			house.setHouseType(null);
			houses.add(house);
		}
		return houses;
	}
}
