package net.jewczuk.openbip.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.SandboxTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SandboxServiceImplTest {
	
	@Autowired
	private SandboxService sandboxService;
	
	@Test
	public void shouldReturnAllSandboxesWhenTheyExist() {
		Long editorID = 2L;
		
		List<SandboxTO> sandboxes = sandboxService.getSandboxesByEditorId(editorID);
		
		assertThat(sandboxes.size()).isEqualTo(2);
	}
	
	@Test
	public void shouldReturnEmptyListWhenEditorHasNoSandboxes() {
		Long editorID = 1L;
		
		List<SandboxTO> sandboxes = sandboxService.getSandboxesByEditorId(editorID);
		
		assertThat(sandboxes).isEmpty();
	}
	
	@Test
	public void shouldReturnEmptyListWhenInvalidEditorId() {
		Long editorID = -1L;
		
		List<SandboxTO> sandboxes = sandboxService.getSandboxesByEditorId(editorID);
		
		assertThat(sandboxes).isEmpty();
	}

}
