package net.jewczuk.openbip.service;

import org.springframework.web.multipart.MultipartFile;

import net.jewczuk.openbip.exceptions.BusinessException;

public interface UploadService {

	void saveFile(MultipartFile file) throws BusinessException;
	
	void deleteFile(String fileName) throws BusinessException;
}
