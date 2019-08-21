package net.jewczuk.openbip.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.AttachmentException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.service.UploadService;

@Service
public class UploadServiceImpl implements UploadService {

	@Override
	public void saveFile(MultipartFile file) throws BusinessException {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(ApplicationProperties.UPLOAD_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
		} catch (IOException e) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_ERROR_COPING);
		}
	}

	@Override
	public void deleteFile(String fileName) throws BusinessException {
		Path path = Paths.get(ApplicationProperties.UPLOAD_FOLDER + fileName);
		
        try {
        	Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_ERROR_DELETING);
		}
	}

}
