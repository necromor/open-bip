package net.jewczuk.openbip.validators.impl;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.validators.PasswordValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordValidatorImplTest {
	
	private final String OLD_PASS = "12test34";
	private final String NEW_VALID = "test1234";
	private final String INVALID_LENGTH = "test1";
	private final String INVALID_SYMBOLS = "thisisatest";

	@Autowired
	private PasswordValidator validator;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldThrowExceptionNotEqualsNewPasswords() throws BusinessException {
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.PASSWORDS_MISSMATCH);
		validator.validatePassword(OLD_PASS, OLD_PASS, NEW_VALID);
	}
	
	@Test
	public void shouldThrowExceptionNewEqualsOld() throws BusinessException {
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.PASSWORD_NOT_CHANGED);
		validator.validatePassword(OLD_PASS, OLD_PASS, OLD_PASS);
	}
	
	@Test
	public void shouldThrowExceptionTooShort() throws BusinessException {
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.PASSWORD_TOO_SHORT);
		validator.validatePassword(OLD_PASS, INVALID_LENGTH, INVALID_LENGTH);
	}	
	
	@Test
	public void shouldThrowExceptionOnlySmallLatinLetter() throws BusinessException {
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.PASSWORD_CONTAINS_ONLY_SMALL_LETTERS);
		validator.validatePassword(OLD_PASS, INVALID_SYMBOLS, INVALID_SYMBOLS);
	}
	
	@Test
	public void shouldValidateAllToTrue() throws BusinessException {
		List<String> validPasses = Arrays.asList("vvvalid1", "alsoValid", "iamvalidT00", "CAPITALVALID", "valid_yes");
		List<Boolean> validated = validPasses.stream()
				.map(p -> runValidation(p))
				.collect(Collectors.toList());
		
		assertThat(validated).allMatch(e -> e.equals(Boolean.TRUE));
	}
	
	private boolean runValidation(String pass) {
		boolean result = false;
		
		try {
			result = validator.validatePassword(OLD_PASS, pass, pass);
		} catch (BusinessException e) {
			//not needed result is already false
		}
		
		return result;
	}
	
	
}
