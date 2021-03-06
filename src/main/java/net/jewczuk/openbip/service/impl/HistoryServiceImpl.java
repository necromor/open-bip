package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.HistoryEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.mapper.HistoryMapper;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.repository.HistoryRepository;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.HistoryTO;

@Service
public class HistoryServiceImpl implements HistoryService {
	
	@Autowired
	HistoryMapper historyMapper;
	
	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	EditorRepository editorRepository;


	@Override
	public List<HistoryTO> getAllLogEntriesByEditor(Long editorID) {
		return historyRepository.getAllLogEntriesByEditor(editorID).stream()
				.map(he -> historyMapper.mapToTO(he))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<HistoryTO> getLimitedLogEntriesByEditor(Long editorID, int limit) {
		return historyRepository.getAllLogEntriesByEditor(editorID).stream()
				.map(he -> historyMapper.mapToTO(he))
				.limit(limit)
				.collect(Collectors.toList());
	}

	@Override
	public void createLogEntry(String message, Long editorID) throws BusinessException {
		HistoryTO hto = new HistoryTO.Builder().action(message).build();
		saveLogEntry(hto, editorID);
	}
	
	private HistoryTO saveLogEntry(HistoryTO entry, Long editorID) throws BusinessException {	
		EditorEntity editor = editorRepository.getEditorById(editorID);
		HistoryEntity savedEntity = historyRepository.save(historyMapper.mapToNewEntity(entry, editor));
		
		return historyMapper.mapToTO(savedEntity);		
	}



}
