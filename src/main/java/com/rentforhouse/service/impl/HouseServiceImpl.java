package com.rentforhouse.service.impl;

import java.util.ArrayList;
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
import com.rentforhouse.converter.HouseConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.Comment;
import com.rentforhouse.entity.House;
import com.rentforhouse.entity.HouseType;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.SaveHouseRequest;
import com.rentforhouse.payload.request.SearchHouseRequest;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.HouseGetResponse;
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
				house.setUser(userRepository.findById(request.getId()).orElse(new User()));
				house.setComments(commentRepository.findAllByHouse_id(request.getId()));
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
					storageService.save(request.getImage(), Storage.houses.name());
					house.setImage(request.getImage().getName());
				}
				if (request.getImage2()!= null) {
					storageService.save(request.getImage2(), Storage.houses.name());
					house.setImage2(request.getImage2().getName());
				}
				if (request.getImage3()!= null) {
					storageService.save(request.getImage3(), Storage.houses.name());
					house.setImage3(request.getImage3().getName());
				}
				if (request.getImage4()!= null) {
					storageService.save(request.getImage4(), Storage.houses.name());
					house.setImage4(request.getImage4().getName());
				}
				if (request.getImage5()!= null) {
					storageService.save(request.getImage5(), Storage.houses.name());
					house.setImage5(request.getImage5().getName());
				}
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
						HttpStatus.BAD_REQUEST.name(), new SysError("unable-to-save-image", new ErrorParam("image"))));
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
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError("unable-to-save", new ErrorParam())));
		}
	}

	@Override
	public HouseDto findById(Long id) {
		House house = houseRepository.findById(id).orElse(new House());
		if (house.getId() != null) {
			HouseDto houseDto = houseConverter.convertToDto(house);
			houseDto.setUser(houseDto.setPassword(houseDto.getUser()));
			houseDto.setImage(storageService.getUrlImage(house.getImage()));
			houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
			houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
			houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
			houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
			return houseDto;
		} else {
			return new HouseDto();
		}
	}

	@Override
	public Boolean delete(Long id) {
		try {
			houseRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public HouseGetResponse findAll(int page, int limit) {
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
				return response;
			} else {
				return new HouseGetResponse();
			}

		} catch (Exception e) {
			return new HouseGetResponse();
		}
	}

	@Override
	public HouseGetResponse findAllByUserId(Long userId, int page, int limit) {
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
				return response;
			} else {
				return new HouseGetResponse();
			}
		} catch (Exception e) {
			return new HouseGetResponse();
		}
	}

	@Override
	public List<HouseDto> findHouse(SearchHouseRequest request) {
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
		return houseDtos;
	}

	@Override
	@Transactional
	public Boolean viewPlus(Long id) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if (house.getId() != null) {
				house.setView(house.getView() + 1);
				houseRepository.save(house);
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public HouseGetResponse findHousesByStatus(Boolean status, int page, int limit) {
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
				return response;
			} else {
				return new HouseGetResponse();
			}
		} catch (Exception e) {
			return new HouseGetResponse();
		}
	}

	@Override
	public HouseGetResponse findByTypeId(Long id, int page, int limit) {
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
				return response;
			} else {
				return new HouseGetResponse();
			}
		} catch (Exception e) {
			return new HouseGetResponse();
		}
	}

	@Override
	@Transactional
	public boolean updateStatus(Long id, Boolean status) {
		try {
			House house = new House();
			house = houseRepository.findById(id).orElse(new House());
			if (house.getId() != null) {
				house.setStatus(status);
				houseRepository.save(house);
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	@Override
	public List<HouseDto> findTop5HouseByView() {
		List<HouseDto> houseDtos = new ArrayList<>();
		try {
			List<House> houses = houseRepository.findTop5ThanOrderByViewDesc();
			for (House house : houses) {
				HouseDto houseDto = houseConverter.convertToDto(house);
				houseDto.setImage(storageService.getUrlImage(house.getImage()));
				houseDto.setImage2(storageService.getUrlImage(house.getImage2()));
				houseDto.setImage3(storageService.getUrlImage(house.getImage3()));
				houseDto.setImage4(storageService.getUrlImage(house.getImage4()));
				houseDto.setImage5(storageService.getUrlImage(house.getImage5()));
				houseDtos.add(houseDto);
			}
			return houseDtos;
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}
}
