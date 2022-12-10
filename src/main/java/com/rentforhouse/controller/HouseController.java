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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.HouseRequest;
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
	private HouseConverter houseConverter;

	/* search Houses theo name sắp xếp theo số view gem den */
	@GetMapping
	public ResponseEntity<?> searchHousesByName(@ModelAttribute SearchHouseRequest request) {
		List<HouseDto> houses = new ArrayList<>();
		houses = houseService.findHouse(request);
		if (houses != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houses, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping("/top-5")
	public ResponseEntity<?> findTop5HousesByView() {
		List<HouseDto> houses = new ArrayList<>();
		houses = houseService.findTop5HouseByView();
		if (houses.get(0) != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houses, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findHouseById(@PathVariable(value = "id") Long id) {
		HouseDto houseDto = houseService.findById(id);
		if (houseDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseDto, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> listHouses(@RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit) {
		HouseGetResponse response = houseService.findAll(page, limit);
		if (response.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping("/status/{trueOrfalse}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> findHousesByStatus(@RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit, @PathVariable Boolean trueOrfalse) {
		HouseGetResponse response = houseService.findHousesByStatus(trueOrfalse, page, limit);
		if (response.getTotal_page() != 0 && response.getHouses() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}

	@GetMapping("/typeId/{typeId}")
	public ResponseEntity<?> findHousesByTypeId(@PathVariable Long typeId, @RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit) {
		HouseGetResponse response = houseService.findByTypeId(typeId, page, limit);
		if (response.getTotal_page() != 0 && response.getHouses() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam())));
	}

	@GetMapping("/user")
	public ResponseEntity<?> findAllHouseByUserId(@RequestParam("id") Long id, @RequestParam("page") int page,
			@RequestParam("limit") int limit) {
		HouseGetResponse houseResponse = houseService.findAllByUserId(id, page, limit);
		if (houseResponse.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse("success", houseResponse, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_STAFF')")
	public ResponseEntity<?> saveHouse(@ModelAttribute HouseRequest request, @RequestParam MultipartFile image,
			@RequestParam(required = false) MultipartFile image2, @RequestParam(required = false) MultipartFile image3, @RequestParam(required = false) MultipartFile image4,
			@RequestParam(required = false) MultipartFile image5) {
		return houseService.save(houseConverter.toSaveHouseRequest(request, image, image2, image3, image4, image5));

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_STAFF')")
	public ResponseEntity<?> updateHouse(@ModelAttribute HouseRequest request, @PathVariable("id") Long id,
			@RequestParam Boolean status, @RequestParam(required = false) MultipartFile image, @RequestParam(required = false) MultipartFile image2,
			@RequestParam(required = false) MultipartFile image3, @RequestParam(required = false) MultipartFile image4,
			@RequestParam(required = false) MultipartFile image5) {
		request.setId(id);
		request.setStatus(status);
		return houseService.save(houseConverter.toSaveHouseRequest(request, image, image2, image3, image4, image5));
	}

	@PutMapping("/viewPlus/{id}")
	public ResponseEntity<?> updateView(@PathVariable("id") Long id) {
		if (houseService.viewPlus(id)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), "successfully", HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
				new SysError("house-not-found", new ErrorParam("id"))));

	}

	@PutMapping("/house/{idHouse}/status/{trueOrfalse}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateStatus(@PathVariable("idHouse") Long id,
			@PathVariable("trueOrfalse") Boolean status) {
		if (houseService.updateStatus(id, status)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), "successfully", HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
				new SysError("house-not-found", new ErrorParam("id"))));

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
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
					new MessageResponse("successful delete"), HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id"))));
	}

}
