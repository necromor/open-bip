package net.jewczuk.openbip.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

	void saveFile(MultipartFile file) throws IOException;
}
