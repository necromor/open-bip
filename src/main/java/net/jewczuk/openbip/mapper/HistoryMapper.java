package net.jewczuk.openbip.mapper;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.entity.HistoryEntity;
import net.jewczuk.openbip.to.HistoryTO;

@Component
public class HistoryMapper {

	public HistoryTO mapToTO(HistoryEntity entity) {
		return new HistoryTO.Builder()
				.action(entity.getAction())
				.createdBy(entity.getEditor().getFullName())
				.createdAt(entity.getCreatedAt())
				.build();
	}
	
	public HistoryEntity mapToNewEntity(HistoryTO hto, EditorEntity editor) {
		HistoryEntity entity = new HistoryEntity();
		entity.setAction(hto.getAction());
		entity.setEditor(editor);
		
		return entity;
	}
	
}
