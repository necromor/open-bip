package net.jewczuk.openbip.repository;

import static org.assertj.core.api.Assertions.assertThat;
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
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EditorRepositoryTest {

	@Autowired
	private EditorRepository editorRepository;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldSuccessfullyReturnEditorWhenWalidId() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(1L);
		
		assertThat(editor.getFullName()).isEqualTo(TestConstants.EDITOR_1);
	}
	
	@Test
	public void shouldThrowExceptionWhenIdIsNull() throws BusinessException {
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		editorRepository.getEditorById(null);
	}
	
	@Test
	public void shouldThrowExceptionWhenIdIsInvalid() throws BusinessException {
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		editorRepository.getEditorById(-1L);
	}
	
}
