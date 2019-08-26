package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.SandboxTO;

public interface SandboxValidator {

	boolean validateSandbox(SandboxTO sandbox) throws BusinessException;
}
