package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.SandboxEntity;
import net.jewczuk.openbip.to.SandboxTO;

@Component
public class SandboxMapper {

	public SandboxTO map2TO(SandboxEntity entity) {
		return new SandboxTO(entity.getTitle(), entity.getLink(), entity.getContent());
	}
	
	public SandboxEntity map2NewE(SandboxTO sandbox, EditorEntity editor) {
		SandboxEntity entity = new SandboxEntity();
		entity.setTitle(sandbox.getTitle());
		entity.setContent(sandbox.getContent());
		entity.setLink(sandbox.getLink());
		entity.setEditor(editor);
		return entity;
	}
	
	public SandboxEntity map2Existing(SandboxTO sandbox, SandboxEntity entity) {
		entity.setTitle(sandbox.getTitle());
		entity.setContent(sandbox.getContent());
		
		return entity;
	}
}
