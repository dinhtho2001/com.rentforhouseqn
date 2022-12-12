package com.rentforhouse.common;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constant {

	public static final String BASE_URL = "http://localhost:8086";
	public static final Integer EXPIRATIIN_JWT_MS = 864000000;
	public static final String SECRET_JWT = "bezKoderSecretKey";
	public static final Path rootPath = Paths.get("src/main/resources/assets");
}
