package com.rentforhouse.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.dto.HouseDto;
import com.rentforhouse.entity.House;
import com.rentforhouse.payload.request.HouseRequest;
import com.rentforhouse.payload.request.SaveHouseRequest;

@Component
public class HouseConverter {

	@Autowired
	private ModelMapper modelMapper;

	public HouseDto convertToDto(House houseEntity) {
		HouseDto houseDto = modelMapper.map(houseEntity, HouseDto.class);
		/*
		 * List<HouseType> houseTypes =
		 * houseTypeRepository.findByHouses_Id(houseEntity.getId()); List<Long> typeIds
		 * = new ArrayList<>(); List<String> typeNames = new ArrayList<>();
		 * for(HouseType houseType :houseTypes) { typeIds.add(houseType.getId());
		 * typeNames.add(houseType.getName()); } houseDto.setTypeIds(typeIds);
		 * houseDto.setTypeNames(typeNames);
		 */
		return houseDto;
	}

	public HouseDto convertToDto(SaveHouseRequest saveHouseRequest) {
		HouseDto houseDto = modelMapper.map(saveHouseRequest, HouseDto.class);
		return houseDto;
	}

	public House convertToEntity(Object request) {
		House house = modelMapper.map(request, House.class);
		/*
		 * List<HouseType> houseTypes = new ArrayList<>(); if (houseSaveRequest.getId()
		 * == null) { house.setImage(houseSaveRequest.getFiles().getOriginalFilename());
		 * } for (Long item : houseSaveRequest.getTypeIds()) { HouseType houseType = new
		 * HouseType(); houseType = houseTypeRepository.findById(item) .orElseThrow((()
		 * -> new MyFileNotFoundException("Type Id : " + item + " không tồn tại")));
		 * houseTypes.add(houseType); } house.setHouseTypes(houseTypes);
		 */

		/*
		 * Authentication authentication =
		 * SecurityContextHolder.getContext().getAuthentication(); UserDetailsImpl
		 * userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal(); User user
		 * = userRepository.findById(userDetailsImpl.getId()).get();
		 * house.setUser(user);
		 */
		return house;
	}

	public House convertToEntity(Object request, House house) {
		house = modelMapper.map(request, House.class);
		return house;
	}

	public SaveHouseRequest toSaveHouseRequest(HouseRequest request, MultipartFile image, MultipartFile image2,
			MultipartFile image3, MultipartFile image4, MultipartFile image5) {
		SaveHouseRequest saveHouseRequest = modelMapper.map(request, SaveHouseRequest.class);
		if (image != null) {
			saveHouseRequest.setImage(image);
		}
		if (image2 != null) {
			saveHouseRequest.setImage2(image2);
		}
		if (image3 != null) {
			saveHouseRequest.setImage3(image3);
		}
		if (image4 != null) {
			saveHouseRequest.setImage4(image4);
		}
		if (image5 != null) {
			saveHouseRequest.setImage5(image5);
		}
		return saveHouseRequest;
	}

}
