package net.jewczuk.openbip.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.SandboxException;
import net.jewczuk.openbip.service.SandboxService;
import net.jewczuk.openbip.to.SandboxTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SandboxServiceImplTest {
	
	@Autowired
	private SandboxService sandboxService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
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
	
	@Test
	public void shouldSuccessfullyAddNewSandbox() throws BusinessException {
		SandboxTO sandbox = new SandboxTO(TestConstants.EDITED_TITLE, TestConstants.EDITED_LINK, TestConstants.EMPTY_CONTENT);
		Long editorID = 2L;
		List<SandboxTO> listBefore = sandboxService.getSandboxesByEditorId(editorID);
		
		SandboxTO saved = sandboxService.saveSandbox(sandbox, editorID);
		List<SandboxTO> listAfter = sandboxService.getSandboxesByEditorId(editorID);
		
		assertThat(saved.getTitle()).isEqualTo(TestConstants.EDITED_TITLE);
		assertThat(listBefore.size() - listAfter.size()).isEqualTo(-1);
		assertThat(listAfter.get(listAfter.size() - 1).getLink()).isEqualTo(TestConstants.EDITED_LINK);
	}
	
	@Test
	public void shouldCorrectlyGetSandboxByLink() throws BusinessException {
		SandboxTO sandbox = sandboxService.getSandboxByLink(TestConstants.SANDBOX_LINK_1);
		
		assertThat(sandbox.getTitle()).isEqualTo(TestConstants.SANDBOX_TITLE_1);
		assertThat(sandbox.getContent()).isEqualTo(TestConstants.SANDBOX_CONTENT_1);
		assertThat(sandbox.getLink()).isEqualTo(TestConstants.SANDBOX_LINK_1);
	}
	
	@Test
	public void shouldThrowExceptionWhenInvalidSandboxLink() throws BusinessException {
		
		excE.expect(SandboxException.class);
		excE.expectMessage(ExceptionsMessages.SANDBOX_INVALID_LINK);
		sandboxService.getSandboxByLink(TestConstants.SANDBOX_LINK_INVALID);
	}

}
