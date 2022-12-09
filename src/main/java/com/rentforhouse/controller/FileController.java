package com.rentforhouse.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.FilesStorageService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	@Autowired
	private FilesStorageService service;

	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
		FileUploadResponse response = new FileUploadResponse();
		response = service.save(multipartFile, "");
		if (response.getDownloadUrl() != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
					response, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("", new ErrorParam(""))));
	}

	@GetMapping("/downloadFile/{filename}")
	public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename) {
		Resource resource = service.download(filename);
		if (resource != null) {
			String contentType = "application/octet-stream";
			String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
		}
		
		return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		
	}

	@GetMapping("/{filename}")
	public ResponseEntity<?> getfile(@PathVariable("filename") String filename) throws IOException {
		byte[] imageData = service.GetImage(filename);
		if (imageData.length != 0) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("", new ErrorParam(""))));
		}
		
	}
	
	@GetMapping("/load/{filename}")
	public ResponseEntity<?> loadfile(@PathVariable("filename") String filename) throws IOException {
		Resource imageData = service.load(filename);
		if (imageData.getURI() != null) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("", new ErrorParam(""))));
		}
		
	}
}
