package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.AttachmentHistoryEntity;
import net.jewczuk.openbip.to.AttachmentHistoryTO;

@Component
public class AttachmentHistoryMapper {

	public AttachmentHistoryTO mapToTO(AttachmentHistoryEntity history) {
		return new AttachmentHistoryTO.Builder()
				.log(history.getLog())
				.createdAt(history.getCreatedAt())
				.createdBy(history.getEditor().getFullName())
				.build();
	}
	
}
