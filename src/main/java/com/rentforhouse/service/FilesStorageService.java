package com.rentforhouse.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import com.rentforhouse.dto.FileInfo;
import com.rentforhouse.payload.response.FileUploadResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {


	FileUploadResponse save(MultipartFile file, String lastContainerName);

	Resource download(String filename);

	void deleteAll();

	Stream<Path> loadAll();

	List<FileInfo> getListFile();

	byte[] GetImage(String fileName)throws IOException;

	String getUrlImage(String fileName);

	Resource load(String filename);
}
