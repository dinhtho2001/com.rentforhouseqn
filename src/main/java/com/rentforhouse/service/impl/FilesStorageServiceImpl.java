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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.rentforhouse.common.Constant;
import com.rentforhouse.common.Param;
import com.rentforhouse.common.Storage;
import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.exception.ErrorParam;
import com.rentforhouse.exception.SysError;
import com.rentforhouse.payload.response.ErrorResponse;
import com.rentforhouse.payload.response.FileUploadResponse;
import com.rentforhouse.payload.response.SuccessReponse;
import com.rentforhouse.service.FilesStorageService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	/*
	 * @Value("classpath:assets") private Resource resource;
	 */

	// private static Path rootPath = null;
	private static final Path rootPath = Paths.get("src/main/resources/assets");

	@Override
	@Transactional
	public ResponseEntity<?> save(MultipartFile file, String lastContainerName) {
		/*
		 * try { rootPath = Paths.get(resource.getURI()); } catch (IOException e1) {
		 * e1.printStackTrace(); return
		 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new
		 * ErrorResponse(HttpStatus.BAD_REQUEST.name(), new
		 * SysError("khong lay dc rootPath", new ErrorParam("")))); }
		 */
		Path path = null;
		String lastPath = null;
		switch (lastContainerName) {
		case "users":

			lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\users";
			path = Paths.get(lastPath);
			break;

		case "houses":
			lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\houses";
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
				// Path filePath2 = rootPathResource.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				// Files.copy(inputStream, filePath2, StandardCopyOption.REPLACE_EXISTING);
				FileUploadResponse response = new FileUploadResponse();
				response.setFileName(fileName);
				response.setSize(file.getSize());
				response.setType(file.getContentType());
				response.setDownloadUrl("/api/file/downloadFile/" + fileName);
				response.setUrl(Constant.BASE_URL + "/api/file/load/" + fileName);
				return ResponseEntity.status(HttpStatus.OK)
						.body(response);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError(
								"Could not save file: " + fileName + ",\nError: " + e.toString(), new ErrorParam(""))));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
					new SysError("Error: " + e.toString(), new ErrorParam(""))));
		}
	}

	@Override
	public String getUrlImage(String fileName) {
		return Constant.BASE_URL + "/api/file/load/" + fileName;
	}

	@Override
	public Resource download(String filename) {
		/*
		 * try { rootPath = Paths.get(resource.getURI()); } catch (IOException e1) {
		 * e1.printStackTrace();
		 * 
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(new
		 * ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError(
		 * "khong lay dc rootPath", new ErrorParam(""))));
		 * 
		 * }
		 */
		Path path = null;
		String lastPath = null;
		try {
			Path file = null;
			if (filename.contains(Storage.users.name())) {
				lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\users";
				path = Paths.get(lastPath);
				file = path.resolve(filename);
			}
			if (filename.contains(Storage.houses.name())) {
				lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\houses";
				path = Paths.get(lastPath);
			} else {
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
		/*
		 * try { rootPath = Paths.get(resource.getURI()); } catch (IOException e1) {
		 * e1.printStackTrace();
		 * 
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(new
		 * ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError(
		 * "khong lay dc rootPath", new ErrorParam(""))));
		 * 
		 * }
		 */
		FileSystemUtils.deleteRecursively(rootPath.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			// rootPath = Paths.get(resource.getURI());
			return Files.walk(rootPath, 1).filter(path -> !path.equals(rootPath)).map(rootPath::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public byte[] GetImage(String filename) throws IOException {
		/*
		 * try { //rootPath = Paths.get(resource.getURI()); } catch (IOException e1) {
		 * e1.printStackTrace();
		 * 
		 * return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(new
		 * ErrorResponse(HttpStatus.BAD_REQUEST.name(), new SysError(
		 * "khong lay dc rootPath", new ErrorParam(""))));
		 * 
		 * }
		 */
		Path path = null;
		String lastPath = null;
		if (filename.contains(Storage.users.name())) {
			lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\users";
			path = Paths.get(lastPath);
		} else if (filename.contains(Storage.houses.name())) {
			lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\houses";
			path = Paths.get(lastPath);
		} else {
			path = rootPath;
		}
		String filePath = (path + "\\" + filename);
		byte[] image = Files.readAllBytes(new File(filePath).toPath());
		return image;
	}

	public ResponseEntity<?> getListFile() {
		try {
			List<FileInfo> fileInfos = loadAll().map(path -> {
				String filename = path.getFileName().toString();
				String url = getUrlImage(filename);

				return new FileInfo(filename, url);
			}).collect(Collectors.toList());
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessReponse(Param.success.name(), fileInfos, HttpStatus.OK.name()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
					new SysError("Error: " + e.toString(), new ErrorParam(""))));
		}
		
	}

	@Override
	public ResponseEntity<?> load(String filename) {
		try {
			/*
			 * try { rootPath = Paths.get(resource.getURI()); } catch (IOException e) {
			 * e.printStackTrace(); return
			 * ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
			 * HttpStatus.BAD_REQUEST.name(), new SysError("khong lay dc rootPath", new
			 * ErrorParam(""))));
			 * 
			 * }
			 */
			Path path = null;
			String lastPath = null;
			if (filename.contains(Storage.users.name())) {
				lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\users";
				path = Paths.get(lastPath);
			} else if (filename.contains(Storage.houses.name())) {
				lastPath = rootPath.getParent() + "\\" + rootPath.getFileName() + "\\images\\houses";
				path = Paths.get(lastPath);
			} else {
				path = rootPath;
			}
			Path file = path.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG).body(resource);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
						HttpStatus.BAD_REQUEST.name(), new SysError("Could not read the file!", new ErrorParam(""))));
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.name(),
					new SysError("Error: " + e.toString(), new ErrorParam(""))));
		}
	}

	protected void init(Path path) throws IOException {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	protected String renameFile(String fileName, String lastContainerName) {
		String[] arrImg = fileName.split("\\.");
		String duoiFileImg = arrImg[arrImg.length - 1];
		String nameFile = "";
		Random random = new Random();
		for (int i = 0; i < (arrImg.length - 1); i++) {
			if (i == 0) {
				nameFile = arrImg[i];
			} else {
				nameFile += arrImg[i];
			}
		}
		nameFile = lastContainerName + "_" + String.valueOf(random.nextInt()).substring(1) + "_" + System.nanoTime()
				+ "." + duoiFileImg;
		return nameFile;
	}

}
