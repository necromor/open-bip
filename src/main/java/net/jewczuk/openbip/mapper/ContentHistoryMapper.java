package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.to.ContentHistoryTO;

@Component
public class ContentHistoryMapper {

	public ContentHistoryTO mapToTO(ContentHistoryEntity content) {
		return new ContentHistoryTO.Builder()
				.content(content.getContent())
				.createdAt(content.getCreatedAt())
				.createdBy(content.getEditor().getFullName())
				.build();
	}
}
