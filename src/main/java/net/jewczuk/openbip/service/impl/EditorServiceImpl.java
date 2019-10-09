package net.jewczuk.openbip.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

	@Override
	@Transactional
	public EditorTO resetPassword(String email) throws BusinessException {
		return editorMapper.mapToEditorTO(editorRepository.resetPassword(email));
	}

	@Override
	@Transactional
	public EditorTO setStatus(String email, boolean status) throws BusinessException {
		return editorMapper.mapToEditorTO(editorRepository.setStatus(email, status));
	}

	@Override
	public EditorTO getByEmail(String email) throws BusinessException {
		return editorMapper.mapToEditorTO(editorRepository.getByEmail(email));
	}

	@Override
	public EditorTO editEditor(EditorTO editor, String oldEmail) throws BusinessException {
		EditorEntity existing = editorRepository.getByEmail(oldEmail);
		existing = editorMapper.mapToExistingEntity(existing, editor);
		
		try {
			existing = editorRepository.save(existing);
		} catch (Exception e) {
			throw new EditorException(ExceptionsMessages.EMAIL_EXISTS);
		}
		
		return editorMapper.mapToEditorTO(existing);
	}

	@Override
	@Transactional
	public EditorTO changePassword(String email, String oldPass, String newPass) throws BusinessException {
		return editorMapper.mapToEditorTO(editorRepository.changePassword(email, oldPass, newPass));
	}

	@Override
	public boolean isAdminPresent() {
		List<EditorEntity> admin = editorRepository.findAll().stream()
				.filter(e -> e.getRole().equals("ADMIN"))
				.collect(Collectors.toList());
		return !admin.isEmpty();
	}

	@Override
	public void createAdminAccount(EditorTO admin) throws BusinessException {
		if (isAdminPresent()) {
			throw new EditorException(ExceptionsMessages.ADMIN_ALREADY_EXISTS);
		}
		
		try {
			editorRepository.save(editorMapper.mapToNewAdminAccount(admin));
		} catch (Exception e) {
			throw new EditorException(ExceptionsMessages.EMAIL_EXISTS);
		}	
	}

}
