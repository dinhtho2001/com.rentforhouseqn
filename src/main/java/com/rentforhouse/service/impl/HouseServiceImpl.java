package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentforhouse.common.Param;
import com.rentforhouse.common.Storage;
import com.rentforhouse.common.TypeHouse;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.entity.Role;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.SaveHouseRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.HouseGetResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.repository.ICommentRepository;
import com.rentforhouse.repository.IHouseRepository;
import com.rentforhouse.repository.IHouseTypeRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IHouseService;
import com.rentforhouse.utils.SecurityUtils;

@Service
public class HouseServiceImpl implements IHouseService {

	@Autowired
	private IHouseRepository houseRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IHouseTypeRepository houseTypeRepository;

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	private HouseConverter houseConverter;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	FilesStorageService storageService;

	@Override
	@Transactional
	public ResponseEntity<?> save(SaveHouseRequest request) {
		try {
			House house = houseConverter.convertToEntity(request);		
			/* request Save */
			if (request.getId() == null) {
				house.setView(0);
				house.setStatus(false);
				house.setUser(userRepository.findById(SecurityUtils.getPrincipal().getId()).orElse(new User()));
			}else {
				/* request Update */
				Boolean checkRoleUser = false;
				List<Role> roles = userRepository.findById(SecurityUtils.getPrincipal().getId()).orElse(new User()).getRoles();
				for (Role role : roles) {
					if (role.getName().equals(UserRole.ROLE_USER.name())) {
						checkRoleUser=true;
					}
				}
				if (checkRoleUser) {
					Boolean checkHouse = false;
					List<House> houses = houseRepository.findByUser(userRepository.findById(SecurityUtils.getPrincipal().getId()).get());
					for (House h : houses) {
						if (h.getId().equals(request.getId())) {
							checkHouse = true;
							break;
						}
					}
					if (!checkHouse) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
								new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("Ban khong co quyen sua: ", new ErrorParam())));
					}
				}
				House oldHouse = houseRepository.findById(request.getId()).get();
				house.setUser(oldHouse.getUser());
				house.setComments(oldHouse.getComments());
				house.setStatus(oldHouse.getStatus());
				house.setView(oldHouse.getView());
			}
			List<HouseType> typeHouses = new ArrayList<>(); 
			for (TypeHouse item : request.getTypeHouses()) {
				if (item == null) {
					continue;
				}else {
					typeHouses.add(houseTypeRepository.findByCode(item.name()));
				}
			}
			house.setHouseTypes(typeHouses);
			try {
				if (request.getImage()!= null) {
					ResponseEntity<?> uploadResponse = storageService.save(request.getImage(), Storage.houses.name());
					FileUploadResponse fileUploadResponse = (FileUploadResponse) uploadResponse.getBody();
					house.setImage(fileUploadResponse.getFileName());		
				}
				if (request.getImage2()!= null) {
					ResponseEntity<?> uploadResponse = storageService.save(request.getImage2(), Storage.houses.name());
					FileUploadResponse fileUploadResponse = (FileUploadResponse) uploadResponse.getBody();
					house.setImage2(fileUploadResponse.getFileName());
				}
				if (request.getImage3()!= null) {
					ResponseEntity<?> uploadResponse = storageService.save(request.getImage3(), Storage.houses.name());
					FileUploadResponse fileUploadResponse = (FileUploadResponse) uploadResponse.getBody();
					house.setImage3(fileUploadResponse.getFileName());
				}
				if (request.getImage4()!= null) {
					ResponseEntity<?> uploadResponse = storageService.save(request.getImage4(), Storage.houses.name());
					FileUploadResponse fileUploadResponse = (FileUploadResponse) uploadResponse.getBody();
					house.setImage4(fileUploadResponse.getFileName());
				}
				if (request.getImage5()!= null) {
					ResponseEntity<?> uploadResponse = storageService.save(request.getImage5(), Storage.houses.name());
					FileUploadResponse fileUploadResponse = (FileUploadResponse) uploadResponse.getBody();
					house.setImage5(fileUploadResponse.getFileName());
				}
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
						HttpStatus.BAD_REQUEST.name(), new SysError("unable-to-save-image: "+e, new ErrorParam("image"))));
			}
			HouseDto houseDto = houseConverter.convertToDto(houseRepository.save(house));
			houseDto.setUser(houseDto.setPassword(houseDto.getUser()));
			houseDto.setImage(storageService.getUrlImage(house.getImage()));
			houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
			houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
			houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
			houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseDto, HttpStatus.OK.name()));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("unable-to-save: error: "+e, new ErrorParam())));
		}
	}

	@Override
	public ResponseEntity<?> findById(Long id) {
		House house = houseRepository.findById(id).orElse(new House());
		if (house.getId() != null) {
			HouseDto houseDto = houseConverter.convertToDto(house);
			houseDto.setUser(houseDto.setPassword(houseDto.getUser()));
			houseDto.setImage(storageService.getUrlImage(house.getImage()));
			houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
			houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
			houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
			houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseDto, HttpStatus.OK.name()));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
		}
	}

	@Override
	public ResponseEntity<?> delete(Long id) {
		try {
			houseRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
					new MessageResponse("successful delete"), HttpStatus.OK.name()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("id-not-found", new ErrorParam("id"))));
		}
	}

	@Override
	public ResponseEntity<?> findAll(int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		Page<House> houseEntities = null;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdDate").descending());
			houseEntities = houseRepository.findAll(pageable);
			if (houseEntities.hasContent()) {
				for (House house : houseEntities) {
					houseDto = houseConverter.convertToDto(house);
					houseDto.getUser().setPassword(null);
					houseDto.setImage(storageService.getUrlImage(house.getImage()));
					houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
					houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
					houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
					houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
					houseDtos.add(houseDto);
				}
				if (houseDtos.get(0).getId() != null) {
					response.setHouses(houseDtos);
					response.setPage(page);
					response.setTotal_page(houseEntities.getTotalPages());
				}
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
		}
	}

	@Override
	public ResponseEntity<?> findAllByUserId(Long userId, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		Page<House> houseEntities = null;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houseEntities = houseRepository.findByUser_Id(userId, pageable);
			for (House house : houseEntities) {
				houseDto = houseConverter.convertToDto(house);
				houseDto.setUser(null);
				houseDto.setUser(userConverter.convertToDto(house.getUser()));
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houseEntities.getTotalPages());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse("success", response, HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam("id"))));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("error: "+e, new ErrorParam("id"))));
		}
	}

	@Override
	public ResponseEntity<?> findHouse(SearchHouseRequest request) {
		try {
			List<HouseDto> houseDtos = new ArrayList<>();
			List<House> houses = new ArrayList<>();
			if (request.getName() != "") {
				houses = houseRepository.findByNameContaining(request.getName());
			} else {
				houses = houseRepository.findAll();
			}
			for (House house : houses) {
				HouseDto houseDto = houseConverter.convertToDto(house);
				houseDto.setUser(userConverter.convertToDto(house.getUser()));
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseDtos, HttpStatus.OK.name()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<?> viewPlus(Long id) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if (house.getId() != null) {
				house.setView(house.getView() + 1);
				houseRepository.save(house);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), "successfully", HttpStatus.OK.name()));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
				new SysError("house-not-found", new ErrorParam("id"))));
	}

	@Override
	public ResponseEntity<?> findHousesByStatus(Boolean status, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		List<HouseDto> houseDtos = new ArrayList<>();
		Page<House> houses = null;
		HouseDto houseDto = new HouseDto();
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houses = houseRepository.findByStatus(status, pageable);
			for (House house : houses) {
				houseDto = houseConverter.convertToDto(house);
				houseDto.getUser().setPassword(null);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houses.getTotalPages());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
		}
	}

	@Override
	public ResponseEntity<?> findByTypeId(Long id, int page, int limit) {
		HouseGetResponse response = new HouseGetResponse();
		Page<House> houses = null;
		List<HouseDto> houseDtos = new ArrayList<>();
		HouseDto houseDto;
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			houses = houseRepository.findByHouseTypes_Id(id, pageable);
			for (House house : houses) {
				houseDto = houseConverter.convertToDto(house);
				houseDto.getUser().setPassword(null);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			if (houseDtos.get(0).getId() != null) {
				response.setHouses(houseDtos);
				response.setPage(page);
				response.setTotal_page(houses.getTotalPages());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), response, HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam())));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("not-found", new ErrorParam())));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<?> updateStatus(Long id, Boolean status) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if (house.getId() != null) {
				house.setStatus(status);
				houseRepository.save(house);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new SuccessReponse(Param.success.name(), "successfully", HttpStatus.OK.name()));
			}
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
					new SysError("house-not-found", new ErrorParam("id"))));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
				new SysError("house-not-found", new ErrorParam("id"))));
	}

	@Override
	public ResponseEntity<?> findTop6HouseByView() {
		List<HouseDto> houseDtos = new ArrayList<>();
		try {
			List<House> houses = houseRepository.findTopThanOrderByViewDesc(6);
			for (House house : houses) {
				HouseDto houseDto = houseConverter.convertToDto(house);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), houseDtos, HttpStatus.OK.name()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("error: "+e.toString(), new ErrorParam())));
		}
	}
}
