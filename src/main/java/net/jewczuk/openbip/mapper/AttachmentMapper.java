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
				.addedBy(attachment.getAddedBy().getFirstName())
				.addedAt(attachment.getCreatedAt())
				.build();
	}
}
