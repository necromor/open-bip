package net.jewczuk.openbip.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	@Autowired
	private PasswordEncoder encoder;
	
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
	
	@Test
	public void shouldReturnAllEditorsOnly() throws BusinessException {
		List<EditorEntity> editors = editorRepository.getOnlyEditors();
		EditorEntity admin = editorRepository.getEditorById(4L);
		
		assertThat(editors.size()).isEqualTo(3);
		assertThat(editors).doesNotContain(admin);
		assertThat(editors).allMatch(e -> e.getRole().equals("EDITOR"));
	}
	
	@Test
	public void shouldResetPassword() throws BusinessException {
		EditorEntity reseted = editorRepository.resetPassword(TestConstants.EDITOR_EMAIL_3);
		
		assertThat(encoder.matches(reseted.getEmail(), reseted.getPassword())).isTrue();
	}
	
	@Test
	public void shouldThrowExceptionWhenResetingInvalidEmail() throws BusinessException {
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		editorRepository.resetPassword(TestConstants.EDITOR_VALID_EMAIL);
	}
	
	@Test
	public void shouldSetActiveToFalse() throws BusinessException {
		EditorEntity saved = editorRepository.setStatus(TestConstants.EDITOR_EMAIL_3, false);
		
		assertThat(saved.isActive()).isFalse();
	}
	
	@Test
	public void shouldSetActiveToTrue() throws BusinessException {
		EditorEntity saved = editorRepository.setStatus(TestConstants.EDITOR_EMAIL_3, false);
		saved = editorRepository.setStatus(TestConstants.EDITOR_EMAIL_3, true);
		
		assertThat(saved.isActive()).isTrue();
	}
	
}
