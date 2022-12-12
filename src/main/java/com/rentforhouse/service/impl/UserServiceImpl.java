package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Param;
import com.rentforhouse.common.Storage;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.MessageResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IUserService;
import com.rentforhouse.utils.SecurityUtils;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private RoleConverter roleConverter;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private FilesStorageService fileService;

	@Override
	@Transactional
	public ResponseEntity<?> save(UserRequest request, MultipartFile image) {
		UserDto userDto = new UserDto();
		/* sửa */
		if (request.getId() != null) {
			userDto.setId(request.getId());
		}
		/* save */
		else {
			if (userRepository.existsByUserName(request.getUserName())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-username", new ErrorParam(Param.username.name()))));
			}
			if (userRepository.existsByEmail(request.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-email", new ErrorParam(Param.email.name()))));
			}
			if (userRepository.existsByPhone(request.getPhone())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.name(),
						new SysError("exist-phone", new ErrorParam(Param.phone.name()))));
			}
		}
		try {
			FileUploadResponse fileUploadResponse = fileService.save(image, Storage.users.name());
			List<RoleDto> roleDtos = new ArrayList<>();
			for (UserRole item : request.getRoles()) {
				if (item == null) {
					continue;
				} else {
					roleDtos.add(roleConverter.convertToDto(roleRepository.findByName(item.name())));
				}
			}
			userDto.setLastName(request.getLastName());
			userDto.setFirstName(request.getFirstName());
			userDto.setUserName(request.getUserName());
			userDto.setEmail(request.getEmail());
			userDto.setPhone(request.getPhone());
			userDto.setImage(fileUploadResponse.getFileName());
			userDto.setPassword(passwordEncoder.encode(request.getPassword()));
			userDto.setStatus(request.getStatus());
			userDto.setRoles(roleDtos);
			User user = userRepository.save(userConverter.convertToEntity(userDto));
			if (user.getId() != null) {
				return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
						userConverter.convertToDto(user), HttpStatus.OK.name()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError()));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError(e.toString(), new ErrorParam())));
		}
	}

	@Override
	public UserDto findbyId(Long id) {
		User user = userRepository.findById(id).get();
		UserDto userDto = userConverter.convertToDto(user);
		userDto.setPassword("co cai nit");
		userDto.setImage(fileService.getUrlImage(userDto.getImage()));
		return userDto;
	}

	@Override
	public DataGetResponse findAll(int page, int limit) {
		DataGetResponse dataGetResponse = new DataGetResponse();
		Pageable pageable = PageRequest.of(page - 1, limit);
		Page<User> users = userRepository.findAll(pageable);
		List<UserDto> userDtos = new ArrayList<>();
		for (User user : users) {
			UserDto userDto = new UserDto();
			userDto = userConverter.convertToDto(user);
			userDto.setImage(fileService.getUrlImage(userDto.getImage()));
			userDtos.add(userDto);
		}

		if (userDtos.get(0) != null) {
			dataGetResponse.setTotal_page(users.getTotalPages());
			dataGetResponse.setPage(page);
			dataGetResponse.setData(userDtos);
			return dataGetResponse;
		} else {
			return new DataGetResponse();
		}
	}

	@Override
	@Transactional
	public ResponseEntity<?> delete(Long id) {
		try {
			/* không được xóa id đang đăng nhập */
			if (SecurityUtils.getPrincipal().getId().equals(id)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
								new SysError("you-do-not-have-permission-to-delete", new ErrorParam())));
			} else {
				List<String> roles = new ArrayList<>();
				for (GrantedAuthority authority : SecurityUtils.getPrincipal().getAuthorities()) {
					roles.add(authority.getAuthority());
				}
				Boolean checkRoleAdmin = false;
				for (String strRole : roles) {
					if (strRole.equals(UserRole.ROLE_ADMIN.name())) {
						checkRoleAdmin = true;
					}
				}
				/* admin được xóa mọi user */
				if (checkRoleAdmin) {
					userRepository.deleteById(id);
					return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
							new MessageResponse("successful delete"), HttpStatus.OK.name()));
				} else {
					for (String item : roles) {
						switch (item) {

						/* không được xóa id cùng cấp */
						case "ROLE_STAFF":
							System.out.println("đã vào ROLE_STAFF");
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
											new SysError("you-do-not-have-permission-to-delete", new ErrorParam())));

						case "ROLE_USER":
							userRepository.deleteById(id);
							return ResponseEntity.status(HttpStatus.OK).body(new SuccessReponse(Param.success.name(),
									new MessageResponse("successful delete"), HttpStatus.OK.name()));
						default:
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
									HttpStatus.BAD_REQUEST.name(), new SysError("can-not-delete", new ErrorParam())));
						}
					}
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
							HttpStatus.BAD_REQUEST.name(), new SysError("can-not-delete", new ErrorParam())));
				}
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
					new SysError("error: " + e.toString(), new ErrorParam())));
		}
	}

	@Override
	@Transactional
	public FileUploadResponse updateImage(Long id, MultipartFile file) {
		FileUploadResponse fileResponse = fileService.save(file, Storage.users.name());
		User user = userRepository.findById(id).orElse(new User());
		user.setImage(fileResponse.getFileName());
		try {
			userRepository.save(user);
		} catch (Exception e) {
			return new FileUploadResponse();
		}

		return fileResponse;
	}

	@Override
	public ResponseEntity<?> updateRoles(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
