package net.jewczuk.openbip.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.repository.CustomEditorRepository;

public class CustomEditorRepositoryImpl implements CustomEditorRepository {
	
	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public EditorEntity getEditorById(Long id) throws BusinessException {
		
		if (id == null) {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}

		EditorEntity entity = entityManager.find(EditorEntity.class, id);
		if (entity == null) {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}
		
		return entity;
	}

}
