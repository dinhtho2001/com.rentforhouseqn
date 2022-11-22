package com.rentforhouse.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.HouseGetResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;
import com.rentforhouse.utils.ValidateUtils;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "houseAPIOfWeb")
@RequestMapping("/api/houses")
public class HouseController {

	@Autowired
	private IHouseService houseService;

	@Autowired
	FilesStorageService storageService;
	
	@GetMapping
	public ResponseEntity<?> searchHousesByName(@ModelAttribute SearchHouseRequest request) {
		List<HouseDto> houses = new ArrayList<>();
		houses = houseService.findHouse(request);
		if (houses != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", houses, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findHouseById(@PathVariable(value = "id") Long id) {
		HouseDto houseDto = houseService.findById(id);
		if (houseDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", houseDto, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> listHouses(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
		HouseGetResponse response = houseService.findAll(page, limit);
		if (response.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping("/status/{trueOrfalse}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> findHousesByStatus(@RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit, @PathVariable Boolean trueOrfalse) {
		HouseGetResponse response = houseService.findHousesByStatus(trueOrfalse, page, limit);
		if (response.getTotal_page() != 0 && response.getHouses() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping("/typeId/{typeId}")
	public ResponseEntity<?> findHousesByTypeId(@PathVariable Long typeId, @RequestParam(name = "page") int page, @RequestParam(name = "limit") int limit) {
		HouseGetResponse response = houseService.findByTypeId(typeId, page, limit);
		if (response.getTotal_page() != 0 && response.getHouses() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam())));
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> saveHouse(@ModelAttribute HouseSaveRequest request
	/* ,@RequestParam MultipartFile file */) {
		/* houseSaveRequest.setFiles(file); */
		ValidateUtils.validateHouse(request);
		HouseDto houseDto = houseService.save(request);
		/*
		 * try { storageService.save(file); } catch (Exception e) { return
		 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
		 * ResponseMessage("Add Image Failed!")); }
		 */
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse("success", houseDto, HttpStatus.OK.name()));

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> updateHouse(@ModelAttribute HouseSaveRequest request, @PathVariable("id") Long id) {
		request.setId(id);
		ValidateUtils.validateHouse(request);
		HouseDto houseDto = houseService.save(request);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse("success", houseDto, HttpStatus.OK.name()));
	}
	
	@PutMapping("/{id}/viewPlus")
	public ResponseEntity<?> updateView(@PathVariable("id") Long id) {
		if (houseService.viewPlus(id)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", null, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("house-not-found", new ErrorParam("id"))));

	}

	/*
	 * @PostMapping("/upload") public ResponseEntity<?> upload(@RequestParam
	 * MultipartFile file){ String message = ""; try { storageService.save(file);
	 * message = "Add file success!"; return
	 * ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message)); }
	 * catch (Exception e) { message = "Failed!"; return
	 * ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new
	 * MessageResponse(message)); }
	 * 
	 * }
	 * 
	 * @GetMapping("/files") public ResponseEntity<List<FileInfo>> getListFiles() {
	 * List<FileInfo> fileInfos = storageService.loadAll().map(path -> { String
	 * filename = path.getFileName().toString(); String url =
	 * MvcUriComponentsBuilder .fromMethodName(HouseController.class, "getFile",
	 * path.getFileName().toString()).build().toString();
	 * 
	 * return new FileInfo(filename, url); }).collect(Collectors.toList());
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(fileInfos); }
	 * 
	 * @GetMapping("/files/{filename:.+}") public ResponseEntity<Resource>
	 * getFile(@PathVariable String filename) { Resource file =
	 * storageService.load(filename); return ResponseEntity.ok()
	 * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
	 * file.getFilename() + "\"") .body(file); }
	 */

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteHouseById(@PathVariable("id") Long id) {
		if (houseService.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new SuccessReponse("success", new MessageResponse("successful delete"), HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id"))));
	}

	
}
