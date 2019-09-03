package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.SandboxEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.SandboxException;
import net.jewczuk.openbip.mapper.SandboxMapper;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.repository.SandboxRepository;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.SandboxTO;
import net.jewczuk.openbip.validators.SandboxValidator;

@Service
public class SandboxServiceImpl implements SandboxService {
	
	@Autowired
	private SandboxRepository sandboxRepository;
	
	@Autowired
	private SandboxMapper sandboxMapper;
	
	@Autowired
	private SandboxValidator sandboxValidator;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private HistoryService historyService;

	@Override
	public List<SandboxTO> getSandboxesByEditorId(Long editorID) {
		return sandboxRepository.findAllByEditorId(editorID).stream()
				.map(e -> sandboxMapper.mapToTO(e))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public SandboxTO saveSandbox(SandboxTO sandbox, Long editorID) throws BusinessException {
		
		sandboxValidator.validateSandbox(sandbox);
		EditorEntity editor = editorRepository.getEditorById(editorID);
		SandboxEntity newE = sandboxMapper.mapToNewEntity(sandbox, editor);
		
		SandboxEntity saved = sandboxRepository.save(newE);
		historyService.createLogEntry(LogMessages.SANDBOX_ADDED + saved.getTitle(), editorID);
		
		return sandboxMapper.mapToTO(saved);
	}
	
	@Override
	@Transactional
	public SandboxTO editSandbox(SandboxTO sandbox, Long editorID) throws BusinessException {
		
		sandboxValidator.validateSandbox(sandbox);
		SandboxEntity existing = sandboxRepository.findByLink(sandbox.getLink());
		checkIfExist(existing);
		SandboxEntity newE = sandboxMapper.mapToExistingEntity(sandbox, existing);
		
		SandboxEntity saved = sandboxRepository.save(newE);
		historyService.createLogEntry(LogMessages.SANDBOX_EDITED + saved.getTitle(), editorID);
		
		return sandboxMapper.mapToTO(saved);
	}

	@Override
	public SandboxTO getSandboxByLink(String link) throws BusinessException {
		SandboxEntity entity = sandboxRepository.findByLink(link);
		checkIfExist(entity);
		return sandboxMapper.mapToTO(entity);
	}
	
	private void checkIfExist(SandboxEntity entity) throws BusinessException {
		if (entity == null) {
			throw new SandboxException(ExceptionsMessages.SANDBOX_INVALID_LINK);
		}
	}

}
