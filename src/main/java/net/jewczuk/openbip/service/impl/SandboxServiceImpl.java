package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.SandboxEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
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
				.map(e -> sandboxMapper.map2TO(e))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public SandboxTO saveSandbox(SandboxTO sandbox, Long editorID) throws BusinessException {
		
		sandboxValidator.validateSandbox(sandbox);
		EditorEntity editor = editorRepository.getEditorById(editorID);
		SandboxEntity newE = sandboxMapper.map2NewE(sandbox, editor);
		
		SandboxEntity saved = sandboxRepository.save(newE);
		historyService.createLogEntry(LogMessages.SANDBOX_ADDED + saved.getTitle(), editorID);
		
		return sandboxMapper.map2TO(saved);
	}

}
