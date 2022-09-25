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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.response.ResponseMessage;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;

@RestController(value="houseAPIOfWeb")
@RequestMapping("/api/house")
public class HouseController {
	
	@Autowired
	private IHouseService houseService;
	
	 @Autowired
	 FilesStorageService storageService;

	@GetMapping
	public List<HouseDto> findHouse(@ModelAttribute HouseRequest houseRequest ){	
		Pageable pageable = PageRequest.of(houseRequest.getPage(), houseRequest.getLimit());
		return houseService.findHouse(houseRequest,pageable);
	}
	
	@PostMapping
	public ResponseEntity<ResponseMessage>  saveHouse(@ModelAttribute HouseDto houseDto){
		String message = "";
		try {
			storageService.save(houseDto.getFile());
			houseService.saveHouse(houseDto);
			message = "Add house success!";
		    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Failed!";
		    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}
	
	/*@PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file ,
			  											@RequestParam("name") String name) {
	    String message = "";
	    try {
	      storageService.save(file); //service

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }*/

	  @GetMapping("/files")
	  public ResponseEntity<List<FileInfo>> getListFiles() {
	    List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
	      String filename = path.getFileName().toString();
	      String url = MvcUriComponentsBuilder
	          .fromMethodName(HouseController.class, "getFile", path.getFileName().toString()).build().toString();

	      return new FileInfo(filename,url);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	  }

	  @GetMapping("/files/{filename:.+}")
	  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
	    Resource file = storageService.load(filename);
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	  }
	
}
