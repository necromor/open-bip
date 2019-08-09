package net.jewczuk.openbip.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.jewczuk.openbip.entity.HistoryEntity;
import net.jewczuk.openbip.repository.CustomHistoryRepository;

public class CustomHistoryRepositoryImpl implements CustomHistoryRepository {
	
	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public List<HistoryEntity> getAllLogEntriesByEditor(Long editorID) {
		return entityManager.createNamedQuery(HistoryEntity.FIND_ALL_LOG_ENTRIES_BY_EDITOR_ID, HistoryEntity.class)
				.setParameter("id", editorID)
				.getResultList();
	}

}
