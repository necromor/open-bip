package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.to.AttachmentTO;

@Component
public class AttachmentMapper {

	public AttachmentTO mapToTO(AttachmentEntity attachment) {
		return new AttachmentTO.Builder()
				.fileName(attachment.getFileName())
				.displayName(attachment.getDisplayName())
				.size(attachment.getSize())
				.extension(attachment.getExtension())
				.addedBy(attachment.getAddedBy().getFullName())
				.addedAt(attachment.getCreatedAt())
				.build();
	}
	
	public AttachmentEntity mapToNewEntity(AttachmentTO ato) {
		AttachmentEntity entity = new AttachmentEntity();
		entity.setDisplayName(ato.getDisplayName());
		entity.setFileName(ato.getFileName());
		entity.setExtension(ato.getExtension());
		entity.setSize(ato.getSize());
		
		return entity;
	}
}
