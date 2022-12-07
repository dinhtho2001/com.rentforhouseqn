package com.rentforhouse.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {

	private String fileName;
    private String downloadUrl;
    private long size;
    private String type;
    private String Url;
}
