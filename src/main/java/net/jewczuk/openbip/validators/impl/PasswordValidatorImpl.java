package net.jewczuk.openbip.validators.impl;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.validators.PasswordValidator;

@Component
public class PasswordValidatorImpl implements PasswordValidator {
	
	private final Pattern INVALID_PATTERN = Pattern.compile("^[a-z]+$");

	@Override
	public boolean validatePassword(String oldPass, String newPass1, String newPass2) 
			throws BusinessException {
		
		checkIfNewPasswordsAreTheSame(newPass1, newPass2);
		checkIfNewPasswordEqualsOld(oldPass, newPass1);
		checkIfNewPasswordLongEnough(newPass1);
		checkIfNewPasswordContainsOnlySmallLatinLetters(newPass1);
		return true;
	}

	private void checkIfNewPasswordsAreTheSame(String newPass1, String newPass2) 
			throws BusinessException {
		
		if (!newPass1.equals(newPass2)) {
			throw new EditorException(ExceptionsMessages.PASSWORDS_MISSMATCH);
		}
	}
	
	private void checkIfNewPasswordEqualsOld(String oldPass, String newPass) 
			throws BusinessException {
		
		if (oldPass.equals(newPass)) {
			throw new EditorException(ExceptionsMessages.PASSWORD_NOT_CHANGED);
		}
	}
	
	private void checkIfNewPasswordLongEnough(String newPass) 
			throws BusinessException {
		
		if (newPass.length() < ApplicationProperties.MIN_PASS_LENGTH) {
			throw new EditorException(ExceptionsMessages.PASSWORD_TOO_SHORT);
		}
	}
	

	private void checkIfNewPasswordContainsOnlySmallLatinLetters(String newPass) 
			throws BusinessException {
		
		if (newPass.matches(INVALID_PATTERN.pattern())) {		
			throw new EditorException(ExceptionsMessages.PASSWORD_CONTAINS_ONLY_SMALL_LETTERS);
		}
	}

}
