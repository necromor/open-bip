package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;

public interface PasswordValidator {

	boolean validatePassword(String oldPass, String newPass1, String newPass2) throws BusinessException;
}
