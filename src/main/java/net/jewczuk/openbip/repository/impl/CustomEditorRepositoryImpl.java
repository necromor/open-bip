package net.jewczuk.openbip.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.repository.CustomEditorRepository;

public class CustomEditorRepositoryImpl implements CustomEditorRepository {
	
	@PersistenceContext
    protected EntityManager entityManager;
	
	@Autowired
	private PasswordEncoder encoder;

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

	@Override
	public List<EditorEntity> getOnlyEditors() {
		return entityManager.createNamedQuery(EditorEntity.FIND_ALL_EDITORS_ONLY, EditorEntity.class)
				.getResultList();
	}

	@Override
	public EditorEntity resetPassword(String email) throws BusinessException {	
		EditorEntity entity = findEditorByEmail(email);

		entity.setPassword(encoder.encode(email));
		entityManager.persist(entity);
		
		return entity;
	}
	
	private EditorEntity findEditorByEmail(String email) throws BusinessException {
		try {
			return entityManager.createNamedQuery(EditorEntity.FIND_BY_EMAIL, EditorEntity.class)
					.setParameter("email", email)
					.getSingleResult();	
		} catch (Exception e) {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}
	}

}
