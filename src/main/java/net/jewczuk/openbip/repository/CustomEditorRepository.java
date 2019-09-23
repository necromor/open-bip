package net.jewczuk.openbip.repository;

import java.util.List;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;

public interface CustomEditorRepository {

	EditorEntity getEditorById(Long id) throws BusinessException;
	
	List<EditorEntity> getOnlyEditors();
	
}
