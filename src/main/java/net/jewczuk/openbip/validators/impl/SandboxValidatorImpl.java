package net.jewczuk.openbip.validators.impl;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.SandboxException;
import net.jewczuk.openbip.to.SandboxTO;
import net.jewczuk.openbip.validators.SandboxValidator;

@Component
public class SandboxValidatorImpl implements SandboxValidator {

	@Override
	public boolean validateSandbox(SandboxTO sandbox) throws BusinessException {
		if (sandbox.getTitle() == null || sandbox.getTitle().length() == 0) {
			throw new SandboxException(ExceptionsMessages.NO_TITLE);
		}
		
		if (sandbox.getTitle().length() < ApplicationProperties.MIN_LINK_LENGHT) {
			throw new SandboxException(ExceptionsMessages.TITLE_MIN_LENGTH);
		}
		
		return true;
	}

}
