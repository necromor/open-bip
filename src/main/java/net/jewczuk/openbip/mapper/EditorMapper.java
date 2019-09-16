package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.RedactorTO;

@Component
public class EditorMapper {

	
	public RedactorTO mapToTO(EditorEntity editor) {
		return new RedactorTO.Builder()
				.firstName(editor.getFirstName())
				.lastName(editor.getLastName())
				.email(editor.getEmail())
				.phone(editor.getPhone())
				.build();
	}
	
	public EditorTO mapToEditorTO(EditorEntity editor) {
		return new EditorTO.Builder()
				.firstName(editor.getFirstName())
				.lastName(editor.getLastName())
				.email(editor.getEmail())
				.phone(editor.getPhone())
				.active(editor.isActive())
				.passGeneric(editor.getFullName().equals(editor.getPassword()))
				.build();
	}
}
