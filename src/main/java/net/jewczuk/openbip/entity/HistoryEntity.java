package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="history")
public class HistoryEntity extends AbstractEntity {

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
