package com.rentforhouse.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.rentforhouse.common.Param;
import com.rentforhouse.dto.HouseTypeDto;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.HouseTypeRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.IHouseTypeService;
import com.rentforhouse.utils.ValidateUtils;

@CrossOrigin(origins = {"http://random-quotes-webs.s3-website-ap-southeast-1.amazonaws.com/", "http://localhost:3000/"})
@RestController(value = "houseTypeAPI")
@RequestMapping("/api/houseTypes")
public class HouseTypeController {
	@Autowired
	private IHouseTypeService houseTypeService;
	

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<HouseTypeDto> houseTypeDtos = houseTypeService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
				houseTypeDtos, HttpStatus.OK.name()));

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable(value = "id") Long id){
		HouseTypeDto houseTypeDto = houseTypeService.findById(id);
		if (houseTypeDto.getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseTypeDto, HttpStatus.OK.name()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> saveHouseType(@ModelAttribute HouseTypeRequest houseTypeRequest){
		if(ValidateUtils.checkNullAndEmpty(houseTypeRequest.getName())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("empty-typename", new ErrorParam("typename"))));
		}
		HouseTypeDto houseTypeDto = houseTypeService.save(houseTypeRequest);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),houseTypeDto,HttpStatus.OK.name()));

	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> updateHouseType(@ModelAttribute HouseTypeRequest houseTypeRequest 
												,@PathVariable(value = "id") Long id){
		if(ValidateUtils.checkNullAndEmpty(houseTypeRequest.getName())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("empty-typename", new ErrorParam("typename"))));
		}
		houseTypeRequest.setId(id);
		HouseTypeDto houseTypeDto = houseTypeService.save(houseTypeRequest);
		if(houseTypeDto.getId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessReponse(Param.success.name(),houseTypeDto,HttpStatus.OK.name()));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<?> deleteHouseTypeById(@PathVariable("id") Long id) {
		if (!houseTypeService.deleteById(id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id"))));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
				new MessageResponse("successful delete"), HttpStatus.OK.name()));
	}
}







