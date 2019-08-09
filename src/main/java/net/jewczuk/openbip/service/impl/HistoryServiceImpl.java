package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.HistoryEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
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
	public HistoryTO saveLogEntry(HistoryTO entry, Long editorID) throws BusinessException {
		
		if (editorID == null) {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}
		
		Optional<EditorEntity> editorO = editorRepository.findById(editorID);

		if (editorO.isPresent()) {
			HistoryEntity savedEntity = historyRepository.save(historyMapper.map2NewHE(entry, editorO.get()));
			return historyMapper.map2TO(savedEntity);		
		} else {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}
	}

	@Override
	public List<HistoryTO> getAllLogEntriesByEditor(Long editorID) {
		return historyRepository.getAllLogEntriesByEditor(editorID).stream()
				.map(he -> historyMapper.map2TO(he))
				.collect(Collectors.toList());
	}

}
