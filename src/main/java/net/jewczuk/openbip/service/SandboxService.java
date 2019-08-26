package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.SandboxTO;

public interface SandboxService {

	List<SandboxTO> getSandboxesByEditorId(Long editorID);
	
	SandboxTO saveSandbox(SandboxTO sandbox, Long editorID) throws BusinessException;

}
