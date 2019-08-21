package net.jewczuk.openbip.validators.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.exceptions.AttachmentException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.repository.AttachmentRepository;
import net.jewczuk.openbip.validators.AttachmentValidator;

@Component
public class AttachmentValidatorImpl implements AttachmentValidator {
	
	@Autowired
	private AttachmentRepository attachmentRepository;

	@Override
	public boolean validateAddAttachment(MultipartFile file, String name) throws BusinessException {
		
		if (file.isEmpty()) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_NO_FILE);
		}
		
		if (name == null || name.length() < ApplicationProperties.MIN_FILENAME_LENGHT) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_INVALID_TITLE);
		}	
		
		if (checkIfNameAlreadyInDB(file.getOriginalFilename())) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_EXISTS);
		}
		
		return true;
	}
	
	private boolean checkIfNameAlreadyInDB(String name) {
		AttachmentEntity entity = new AttachmentEntity();
		entity.setFileName(name);
		
		AttachmentEntity schrodinger = attachmentRepository.findByFileName(name);
		
		return schrodinger != null;
	}

}
