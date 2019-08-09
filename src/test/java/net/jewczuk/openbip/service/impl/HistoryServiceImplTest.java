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
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.HistoryTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HistoryServiceImplTest {

	@Autowired
	HistoryService historyService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldReturnEmptyListWhenNoEntriesForEditor() {
		List<HistoryTO> history = historyService.getAllLogEntriesByEditor(1L);
		
		assertThat(history).isEmpty();
	}
	
	@Test
	public void shouldReturnEmptyListWhenEditorDoesNotExist() {
		List<HistoryTO> history = historyService.getAllLogEntriesByEditor(111L);
		
		assertThat(history).isEmpty();
	}
	
	@Test
	public void shouldReturnCorrectNumberOfSortedEntries() {
		List<HistoryTO> history = historyService.getAllLogEntriesByEditor(2L);
		
		assertThat(history.size()).isEqualTo(2);
		assertThat(history.get(0).getAction()).isEqualTo(TestConstants.LOG_ENTRY_2);
		assertThat(history.get(1).getAction()).isEqualTo(TestConstants.LOG_ENTRY_1);
		assertThat(history.get(0).getCreatedAt()).isAfter(history.get(1).getCreatedAt());
	}
	
	@Test
	public void shouldSuccessfullySaveNewLogEntry() throws BusinessException {
		Long editorID = 3L;
		List<HistoryTO> historyBefore = historyService.getAllLogEntriesByEditor(editorID);
		
		historyService.createLogEntry(TestConstants.LOG_ENTRY_4, editorID);
		List<HistoryTO> historyAfter = historyService.getAllLogEntriesByEditor(editorID);
		
		assertThat(historyAfter.size() - historyBefore.size()).isEqualTo(1);
		assertThat(historyAfter.get(0).getAction()).isEqualTo(TestConstants.LOG_ENTRY_4);
	}
	
	@Test
	public void shouldThrowExceptionWhenSavingAndEditorDoesNotExist() throws BusinessException {		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		historyService.createLogEntry(TestConstants.LOG_ENTRY_4, 121L);
	}
	
	@Test
	public void shouldThrowExceptionWhenSavingAndEditorIsNull() throws BusinessException {		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		historyService.createLogEntry(TestConstants.LOG_ENTRY_4, null);
	}
	
	
}
