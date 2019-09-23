package net.jewczuk.openbip.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.to.EditorTO;
import net.jewczuk.openbip.to.RedactorTO;

@Component
public class EditorMapper {

	@Autowired
	private PasswordEncoder encoder;
	
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
				.passGeneric(encoder.matches(editor.getEmail(), editor.getPassword()))
				.build();
	}
	
	public EditorEntity mapToNewEntity(EditorTO eTO) {
		EditorEntity entity = new EditorEntity();
		entity.setFirstName(eTO.getFirstName());
		entity.setLastName(eTO.getLastName());
		entity.setEmail(eTO.getEmail());
		entity.setPhone(eTO.getPhone());
		entity.setActive(eTO.isActive());
		entity.setRole("EDITOR");
		entity.setPassword(encoder.encode(eTO.getEmail()));
		
		return entity;
	}
}
