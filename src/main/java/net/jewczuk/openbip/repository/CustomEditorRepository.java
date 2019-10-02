package net.jewczuk.openbip.repository;

import java.util.List;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;

public interface CustomEditorRepository {

	EditorEntity getEditorById(Long id) throws BusinessException;
	
	List<EditorEntity> getOnlyEditors();
	
	EditorEntity resetPassword(String email) throws BusinessException;
	
	EditorEntity setStatus(String email, boolean status) throws BusinessException;
	
	EditorEntity getByEmail(String email) throws BusinessException;
	
	EditorEntity changePassword(String email, String oldPass, String newPass) throws BusinessException;
}
