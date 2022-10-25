package com.rentforhouse.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.HouseSaveRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.HouseResponse;
import com.rentforhouse.payload.response.ResponseMessage;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController(value = "houseAPIOfWeb")
@RequestMapping("/api/houses")
public class HouseController {

	@Autowired
	private IHouseService houseService;

	@Autowired
	FilesStorageService storageService;

	@GetMapping
	public ResponseEntity<?> findHouse(@ModelAttribute HouseRequest houseRequest) {
		Pageable pageable = PageRequest.of(houseRequest.getPage(), houseRequest.getLimit());
		HouseResponse houseResponse = houseService.findHouse(houseRequest, pageable);
		if (houseResponse.getHouses() != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success",
					houseResponse, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping("/list/{id}")
	public ResponseEntity<?> findAllHouseById(@RequestParam("id") Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
		if (true) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success",
					null, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> findAllHouseByUserId(@RequestParam("id") Long id, @RequestParam("page") int page, @RequestParam("limit") int limit) {
		HouseResponse houseResponse = houseService.findAllByUserId(id, page, limit);
		if (houseResponse.getTotal_page() != 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success",
					houseResponse, HttpStatus.OK.name()));
		}	
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
	}

	@GetMapping("/{id}")
	public HouseDto findById(@PathVariable(value = "id") Long id) {
		return houseService.findById(id);
	}

	@PostMapping
	public ResponseEntity<?>  saveHouse(@ModelAttribute HouseSaveRequest houseSaveRequest
										,@RequestParam MultipartFile file){
		try {
			houseSaveRequest.setFiles(file);
			HouseDto houseDto = houseService.saveHouse(houseSaveRequest);
			storageService.save(file);
		    return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("Add house success!", houseDto, HttpStatus.OK.name()));
		} catch (Exception e) {
		    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Failed!"));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>  editHouse(@ModelAttribute HouseSaveRequest houseSaveRequest,@PathVariable("id") Long id){
		try {
			houseSaveRequest.setId(id);
			HouseDto houseDto = houseService.saveHouse(houseSaveRequest);
		    return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("Add house success!", houseDto, HttpStatus.OK.name()));
		} catch (Exception e) {
		    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Failed!"));
		}
	}
	
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage>  upload(@RequestParam MultipartFile file){
		String message = "";
		try {
			storageService.save(file);
			message = "Add file success!";
		    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Failed!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(HouseController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		if(houseService.delete(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse("success",
					null, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id")))
				);
		
	}

}
