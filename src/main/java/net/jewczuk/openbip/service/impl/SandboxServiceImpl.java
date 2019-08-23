package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.mapper.SandboxMapper;
import net.jewczuk.openbip.repository.SandboxRepository;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.SandboxTO;

@Service
public class SandboxServiceImpl implements SandboxService {
	
	@Autowired
	private SandboxRepository sandboxRepository;
	
	@Autowired
	private SandboxMapper sandboxMapper;

	@Override
	public List<SandboxTO> getSandboxesByEditorId(Long editorID) {
		return sandboxRepository.findAllByEditorId(editorID).stream()
				.map(e -> sandboxMapper.map2TO(e))
				.collect(Collectors.toList());
	}

}
