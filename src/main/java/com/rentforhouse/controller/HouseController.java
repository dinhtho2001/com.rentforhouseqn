package com.rentforhouse.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.ResourceDTO;
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.service.IExcelService;
import com.rentforhouse.service.IHouseService;

@CrossOrigin(origins = { "http://random-quotes-webs.s3-website-ap-southeast-1.amazonaws.com/",
		"http://localhost:3000/" })
@RestController(value = "houseAPIOfWeb")
@RequestMapping("/api/houses")
public class HouseController {

	@Autowired
	private IHouseService houseService;

	@Autowired
	private HouseConverter houseConverter;

	@Autowired
	private IExcelService excelService;

	/* search Houses theo name sắp xếp theo số view gem den */
	@GetMapping
	public ResponseEntity<?> searchHousesByName(@ModelAttribute SearchHouseRequest request) {
		return houseService.findHouse(request);
	}

	@GetMapping("/top-6")
	public ResponseEntity<?> findTop6HousesByView() {
		return houseService.findTop6HouseByView();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findHouseById(@PathVariable(value = "id") Long id) {
		return houseService.findById(id);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> listHouses(@RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit) {
		return houseService.findAll(page, limit);
	}

	@GetMapping("/status/{trueOrfalse}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> findHousesByStatus(@RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit, @PathVariable Boolean trueOrfalse) {
		return houseService.findHousesByStatus(trueOrfalse, page, limit);
	}

	@GetMapping("/typeId/{typeId}")
	public ResponseEntity<?> findHousesByTypeId(@PathVariable Long typeId, @RequestParam(name = "page") int page,
			@RequestParam(name = "limit") int limit) {
		return houseService.findByTypeId(typeId, page, limit);
	}

	@GetMapping("/user")
	public ResponseEntity<?> findAllHouseByUserId(@RequestParam("id") Long id, @RequestParam("page") int page,
			@RequestParam("limit") int limit) {
		return houseService.findAllByUserId(id, page, limit);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_STAFF')")
	public ResponseEntity<?> saveHouse(@ModelAttribute HouseRequest request, @RequestParam MultipartFile image,
			@RequestParam(required = false) MultipartFile image2, @RequestParam(required = false) MultipartFile image3,
			@RequestParam(required = false) MultipartFile image4,
			@RequestParam(required = false) MultipartFile image5) {
		return houseService.save(houseConverter.toSaveHouseRequest(request, image, image2, image3, image4, image5));

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_STAFF')")
	public ResponseEntity<?> updateHouse(@ModelAttribute HouseRequest request, @PathVariable("id") Long id,
			@RequestParam(required = false) MultipartFile image, @RequestParam(required = false) MultipartFile image2,
			@RequestParam(required = false) MultipartFile image3, @RequestParam(required = false) MultipartFile image4,
			@RequestParam(required = false) MultipartFile image5) {
		request.setId(id);
		return houseService.save(houseConverter.toSaveHouseRequest(request, image, image2, image3, image4, image5));
	}

	@PutMapping("/viewPlus/{id}")
	public ResponseEntity<?> updateView(@PathVariable("id") Long id) {
		return houseService.viewPlus(id);
	}

	@PutMapping("/house/{idHouse}/status/{trueOrfalse}")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> updateStatus(@PathVariable("idHouse") Long id,
			@PathVariable("trueOrfalse") Boolean status) {
		return houseService.updateStatus(id, status);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteHouseById(@PathVariable("id") Long id) {
		return houseService.delete(id);
	}

	@GetMapping("/export/excel")
	@PreAuthorize("hasAnyRole('ROLE_STAFF','ROLE_ADMIN')")
	public ResponseEntity<?> exportToExcel() throws IOException {
		ResourceDTO resourceDTO = excelService.exportHouseToExcel();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Disposition", "attachment; filename=Post_House" + currentDateTime + ".xlsx");
		return ResponseEntity.ok().contentType(resourceDTO.getMediaType()).headers(httpHeaders)
				.body(resourceDTO.getResource());
	}
	
	@PostMapping("import/excel")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> importHousesByExcel(@RequestParam("file") MultipartFile file){
		return excelService.importHouses(file);
	}

}
