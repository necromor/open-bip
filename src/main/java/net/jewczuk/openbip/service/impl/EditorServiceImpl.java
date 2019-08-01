package net.jewczuk.openbip.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.mapper.EditorMapper;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.service.EditorService;
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
				.map(e -> editorMapper.map2TO(e))
				.sorted(Comparator.comparing(RedactorTO::getLastName))
				.collect(Collectors.toList());
	}

}
