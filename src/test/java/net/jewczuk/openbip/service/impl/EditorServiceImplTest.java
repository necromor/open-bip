package net.jewczuk.openbip.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

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
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.RedactorTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EditorServiceImplTest {
	
	@Autowired
	EditorServiceImpl editorService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shoulddisplayAllRedactorsOnly() {
		List<RedactorTO> redactors = editorService.getAllRedactors();
		List<String> fullNames = redactors.stream().map(r -> r.getFullName()).collect(Collectors.toList());
		
		assertThat(fullNames).containsExactly(TestConstants.EDITOR_1, TestConstants.EDITOR_3, TestConstants.EDITOR_2);
	}
	
	@Test
	public void shouldReturnOnlyEditors() {
		List<EditorTO> editors = editorService.getAllEditorsOnly();
		List<String> fullNames = editors.stream().map(r -> r.getFullName()).collect(Collectors.toList());
		
		assertThat(fullNames).containsExactly(TestConstants.EDITOR_1, TestConstants.EDITOR_3, TestConstants.EDITOR_2);
	}
	
	@Test
	public void shouldAddEditor() throws BusinessException {
		EditorTO toSave = new EditorTO.Builder()
				.firstName(TestConstants.EDITOR_FIRSTNAME_1)
				.lastName(TestConstants.EDITOR_LASTNAME_1)
				.phone(TestConstants.EDITOR_PHONE_1)
				.active(false)
				.email(TestConstants.EDITOR_VALID_EMAIL)
				.build();
		
		EditorTO saved = editorService.addNewEditor(toSave);
		
		assertThat(saved.isPassGeneric()).isTrue();
		assertThat(saved.getFirstName()).isEqualTo(TestConstants.EDITOR_FIRSTNAME_1);
	}
	
	@Test
	public void shouldThrowExceptionWhenEMailExists() throws BusinessException {
		EditorTO toSave = new EditorTO.Builder()
				.firstName(TestConstants.EDITOR_FIRSTNAME_1)
				.lastName(TestConstants.EDITOR_LASTNAME_1)
				.phone(TestConstants.EDITOR_PHONE_1)
				.active(false)
				.email(TestConstants.EDITOR_INVALID_EMAIL)
				.build();
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.EMAIL_EXISTS);
		editorService.addNewEditor(toSave);
	}
	
	@Test
	public void shouldResetEditorPassword() throws BusinessException {
		List<EditorTO> allBefore = editorService.getAllEditorsOnly();
		EditorTO before = allBefore.get(0);
		
		EditorTO after = editorService.resetPassword(before.getEmail());
		
		assertThat(before.isPassGeneric()).isFalse();
		assertThat(after.isPassGeneric()).isTrue();
	}
	
	@Test
	public void shouldThrowExceptionWhenResetingInvalidEditor() throws BusinessException {
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		editorService.resetPassword(TestConstants.EDITOR_VALID_EMAIL);
	}

}
