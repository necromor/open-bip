package net.jewczuk.openbip.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.to.RedactorTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EditorServiceImplTest {
	
	@Autowired
	EditorServiceImpl editorService;
	
	@Test
	public void shoulddisplayAllRedactorsOnly() {
		List<RedactorTO> redactors = editorService.getAllRedactors();
		List<String> fullNames = redactors.stream().map(r -> r.getFullName()).collect(Collectors.toList());
		
		assertThat(fullNames).containsExactly(TestConstants.EDITOR_1, TestConstants.EDITOR_3, TestConstants.EDITOR_2);
	}

}
