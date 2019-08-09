package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQueries({
	@NamedQuery(name = HistoryEntity.FIND_ALL_LOG_ENTRIES_BY_EDITOR_ID, query = "SELECT history FROM HistoryEntity history "
			+ " WHERE history.editor.id = :id ORDER BY history.createdAt DESC")})
@Entity
@Table(name="history")
public class HistoryEntity extends AbstractEntity {
	
	public static final String FIND_ALL_LOG_ENTRIES_BY_EDITOR_ID = "findAllLogEntriesByEditorId";

	@ManyToOne
	@JoinColumn(name="editor_id")
	private EditorEntity editor;
	
	@Column(name="action", nullable=false)
	private String action;

	public EditorEntity getEditor() {
		return editor;
	}

	public void setEditor(EditorEntity editor) {
		this.editor = editor;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
