package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.HistoryTO;

public interface HistoryService {

	void createLogEntry(String message, Long editorID) throws BusinessException;
	
	List<HistoryTO> getAllLogEntriesByEditor(Long editorID);
	
}
