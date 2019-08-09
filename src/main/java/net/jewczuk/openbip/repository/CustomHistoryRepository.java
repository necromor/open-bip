package net.jewczuk.openbip.repository;

import java.util.List;

import net.jewczuk.openbip.entity.HistoryEntity;

public interface CustomHistoryRepository {

	List<HistoryEntity> getAllLogEntriesByEditor(Long editorID);
}
