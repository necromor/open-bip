package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.RedactorTO;

public interface EditorService {

	List<RedactorTO> getAllRedactors();
	
	List<EditorTO> getAllEditorsOnly();
	
	EditorTO addNewEditor(EditorTO editor) throws BusinessException;
	
	EditorTO resetPassword(String email) throws BusinessException;
	
	EditorTO setStatus(String email, boolean status) throws BusinessException;
	
	EditorTO getByEmail(String email) throws BusinessException;

	EditorTO editEditor(EditorTO editor, String oldEmail) throws BusinessException;
	
	EditorTO changePassword(String email, String oldPass, String newPass) throws BusinessException;
	
	boolean isAdminPresent();
	
	void createAdminAccount(EditorTO admin) throws BusinessException;
}
