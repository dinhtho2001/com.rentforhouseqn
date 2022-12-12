package com.rentforhouse.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Storage;
import com.rentforhouse.common.UserRole;
import com.rentforhouse.converter.RoleConverter;
import com.rentforhouse.converter.UserConverter;
import com.rentforhouse.dto.RoleDto;
import com.rentforhouse.dto.UserDto;
import com.rentforhouse.entity.User;
import com.rentforhouse.payload.request.UserRequest;
import com.rentforhouse.payload.response.DataGetResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.repository.IRoleRepository;
import com.rentforhouse.repository.IUserRepository;
import com.rentforhouse.service.FilesStorageService;
import com.rentforhouse.service.IUserService;

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
	public UserDto save(UserRequest request, MultipartFile image) {
		UserDto userDto = new UserDto();
		if (userRepository.existsByUserName(request.getUserName())) {
			userDto.setUserName(request.getUserName());
			return userDto;
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			userDto.setEmail(request.getEmail());
			return userDto;
		}
		if (userRepository.existsByPhone(request.getPhone())) {
			userDto.setPhone(request.getPhone());
			return userDto;
		} else {
			if (request.getId() != null) {
				userDto.setId(request.getId());
			}
			try {
				FileUploadResponse fileUploadResponse = fileService.save(image, Storage.users.name());
				List<RoleDto> roleDtos = new ArrayList<>();
				for (UserRole item : request.getRoles()) {
					if (item == null) {
						continue;
					}else {
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
					return userConverter.convertToDto(user);
				} else {
					return new UserDto();
				}
			} catch (Exception e) {
				userDto.setImage("Error");
				return userDto;
			}		
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
	public Boolean delete(Long id) {
		try {
			User user = userRepository.findById(id).orElse(new User());
			if (user.getId() != null) {
				@SuppressWarnings("unchecked")
				Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder
						.getContext().getAuthentication().getAuthorities();
				List<String> roles = new ArrayList<>();
				for (GrantedAuthority authority : authorities) {
					roles.add(authority.getAuthority());
					//
					System.out.println("authority Role: " + authority.getAuthority());
				}		
				for (String strRole : roles) {
					if (strRole.equals(UserRole.ROLE_ADMIN.name())) {
						userRepository.deleteById(id);
						return true;
					}else {
						switch (strRole) {
						case "ROLE_ADMIN":
							System.out.println("đã vào Role ADMIN");			
							return false;
						case "ROLE_STAFF":
							System.out.println("đã vào ROLE_STAFF");			
							return false;

						case "ROLE_USER":
							System.out.println("đã vào ROLE_USER");
							userRepository.deleteById(id);
							return true;

						default:
							return false;
						}
					}
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	@Transactional
	public FileUploadResponse updateImage(Long id, MultipartFile file) {
		FileUploadResponse fileResponse = fileService.save(file, Storage.users.name());
		User user =  userRepository.findById(id).orElse(new User());
		user.setImage(fileResponse.getFileName());
		try {
			userRepository.save(user);
		} catch (Exception e) {
			return new FileUploadResponse();
		}
		
		return fileResponse;
	}

}
