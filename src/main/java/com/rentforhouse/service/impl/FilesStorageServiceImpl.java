package com.rentforhouse.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.rentforhouse.common.Storage;
import com.rentforhouse.controller.HouseController;
import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.service.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	private static final Path rootPath = Paths.get("src/main/resources/assets");

	
	protected void init(Path path) throws IOException {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}
	
	protected String renameFile(String fileName, String lastContainerName){
		String[] arrImg =  fileName.split("\\.");
		String duoiFileImg = arrImg[arrImg.length - 1];
		String nameFile = "";
		Random random = new Random();
		for (int i  = 0; i< (arrImg.length - 1) ; i++) {
			if(i == 0){
				nameFile = arrImg[i];
			}else{
				nameFile += arrImg[i];
			}
		}
		nameFile = lastContainerName + "_"+ String.valueOf(random.nextInt()).substring(1) + "_" + System.nanoTime() + "." + duoiFileImg;
		return nameFile;
	}

	@Override
	@Transactional
	public FileUploadResponse save(MultipartFile file, String lastContainerName) {
		Path path = null;
		String lastPath = null;
		switch (lastContainerName) {
		case "users":
			
			lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\users";
			path = Paths.get(lastPath);
			break;

		case "houses":
			lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\houses";
			System.out.println(lastPath);
			path = Paths.get(lastPath);
			break;
		default:
			path = rootPath;
			break;
		}
		try {
			if (!Files.exists(path)) {
				init(path);
			}
			String fileName = renameFile(file.getOriginalFilename(), lastContainerName);	
			
			try (InputStream inputStream = file.getInputStream()) {	
				Path filePath = path.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);			
				FileUploadResponse response = new FileUploadResponse();
				response.setFileName(fileName);
				response.setSize(file.getSize());
				response.setType(file.getContentType());
				response.setDownloadUrl("/api/file/downloadFile/" + fileName);
				response.setUrl("/api/file/" + fileName);
				return response;
			} catch (IOException e) {
				throw new IOException("Could not save file: " + fileName, e);
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public Resource download(String filename) {	
		Path path = null;
		String lastPath = null;
		try {
			Path file = null;
			if (filename.contains("users")) {
				lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\users";
				path = Paths.get(lastPath);
				file = path.resolve(filename);
			}
			if (filename.contains("houses")) {
				lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\houses";
				path = Paths.get(lastPath);
			}else {
				file = rootPath.resolve(filename);
			}
			
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootPath.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootPath, 1).filter(path -> !path.equals(this.rootPath)).map(this.rootPath::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
	
	@Override
	public byte[] GetImage(String filename) throws IOException{
		Path path = null;
		String lastPath = null;
		if (filename.contains("users")) {
			lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\users";
			path = Paths.get(lastPath);
		}
		if (filename.contains("houses")) {
			lastPath = rootPath.getParent()+"\\"+ rootPath.getFileName() +"\\images\\houses";
			path = Paths.get(lastPath);
		}else {
			path = rootPath;
		}
		String filePath = (path + "\\" + filename);
		byte[] image = Files.readAllBytes(new File(filePath).toPath());
		return image;
	}

	public List<FileInfo> getListFile() {
		List<FileInfo> fileInfos = loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(HouseController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		return fileInfos;
	}

}
