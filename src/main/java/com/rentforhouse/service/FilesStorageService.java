package com.rentforhouse.service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import com.rentforhouse.dto.FileInfo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

	void init();

	void save(MultipartFile file);

	Resource download(String filename);

	void deleteAll();

	Stream<Path> loadAll();

	List<FileInfo> getListFile();
}
