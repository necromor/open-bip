package net.jewczuk.openbip.validators.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.AttachmentException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.validators.AttachmentValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttachmentValidatorImplTest {
	
	@Autowired
	private AttachmentValidator attachmentValidator;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldSuccessfullyValidateValidData() throws BusinessException {
		String fileName = "test.abc";
		MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileName.getBytes());
		
		assertThat(attachmentValidator.validateAddAttachment(file, fileName)).isTrue();
	}
	
	@Test
	public void shouldThrowExceptionWhenTitleIsNull() throws BusinessException {
		String fileName = "test.abc";
		MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileName.getBytes());
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_INVALID_TITLE);
		attachmentValidator.validateAddAttachment(file, null);
	}
	
	@Test
	public void shouldThrowExceptionWhenTitleTooShort() throws BusinessException {
		String fileName = "test.abc";
		MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileName.getBytes());
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_INVALID_TITLE);
		attachmentValidator.validateAddAttachment(file, "a");
	}
	
	@Test
	public void shouldThrowExceptionWhenFileInDB() throws BusinessException {
		String fileName = TestConstants.ATTACHMENT_1_NAME;
		MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", fileName.getBytes());
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_EXISTS);
		attachmentValidator.validateAddAttachment(file, fileName);
	}

}
