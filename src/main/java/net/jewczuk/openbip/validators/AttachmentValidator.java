package net.jewczuk.openbip.validators;

import org.springframework.web.multipart.MultipartFile;

import net.jewczuk.openbip.exceptions.BusinessException;

public interface AttachmentValidator {

	boolean validateAddAttachment(MultipartFile file, String name) throws BusinessException;
}
