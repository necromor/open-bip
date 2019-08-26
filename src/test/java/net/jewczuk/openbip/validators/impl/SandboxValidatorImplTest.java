package net.jewczuk.openbip.validators.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.SandboxException;
import net.jewczuk.openbip.to.SandboxTO;
import net.jewczuk.openbip.validators.SandboxValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SandboxValidatorImplTest {

	@Autowired
	private SandboxValidator sandboxValidator;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldReturnTrueWhenAllIsOK() throws BusinessException {
		SandboxTO sandbox = new SandboxTO(TestConstants.EDITED_TITLE, TestConstants.EDITED_LINK, TestConstants.EMPTY_CONTENT);
		
		boolean result = sandboxValidator.validateSandbox(sandbox);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void shouldThrowExceptionWhenTitleIsNull() throws BusinessException {
		SandboxTO sandbox = new SandboxTO(null, TestConstants.EDITED_LINK, TestConstants.EMPTY_CONTENT);
		
		excE.expect(SandboxException.class);
		excE.expectMessage(ExceptionsMessages.NO_TITLE);
		sandboxValidator.validateSandbox(sandbox);
	}
	
	@Test
	public void shouldThrowExceptionWhenTitleIsEmpty() throws BusinessException {
		SandboxTO sandbox = new SandboxTO(TestConstants.EMPTY_CONTENT, TestConstants.EDITED_LINK, TestConstants.EMPTY_CONTENT);
		
		excE.expect(SandboxException.class);
		excE.expectMessage(ExceptionsMessages.NO_TITLE);
		sandboxValidator.validateSandbox(sandbox);
	}
	
	@Test
	public void shouldThrowExceptionWhenTitleIsTooShort() throws BusinessException {
		SandboxTO sandbox = new SandboxTO("aa", TestConstants.EDITED_LINK, TestConstants.EMPTY_CONTENT);
		
		excE.expect(SandboxException.class);
		excE.expectMessage(ExceptionsMessages.TITLE_MIN_LENGTH);
		sandboxValidator.validateSandbox(sandbox);
	}
	
}
