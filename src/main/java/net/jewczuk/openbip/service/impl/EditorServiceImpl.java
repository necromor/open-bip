package net.jewczuk.openbip.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.mapper.EditorMapper;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.service.EditorService;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.RedactorTO;

@Service
public class EditorServiceImpl implements EditorService {
	
	@Autowired
	EditorMapper editorMapper;
	
	@Autowired
	EditorRepository editorRepository;

	@Override
	public List<RedactorTO> getAllRedactors() {
		return editorRepository.findAll().stream()
				.filter(e -> e.getRole().equals("EDITOR"))
				.map(e -> editorMapper.mapToTO(e))
				.sorted(Comparator.comparing(RedactorTO::getLastName))
				.collect(Collectors.toList());
	}

	@Override
	public List<EditorTO> getAllEditorsOnly() {
		return editorRepository.getOnlyEditors().stream()
				.map(e -> editorMapper.mapToEditorTO(e))
				.sorted(Comparator.comparing(EditorTO::getLastName))
				.collect(Collectors.toList());
	}

	@Override
	public EditorTO addNewEditor(EditorTO editor) throws BusinessException {
		EditorEntity saved;
		
		try {
			saved = editorRepository.save(editorMapper.mapToNewEntity(editor));
		} catch (Exception e) {
			throw new EditorException(ExceptionsMessages.EMAIL_EXISTS);
		}	
		
		return editorMapper.mapToEditorTO(saved);
	}

}
