package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="atachment_history")
public class AtachmentHistoryEntity extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="editor_id")
	private EditorEntity editor;
	
	@Column(name="log", nullable=false)
	private String log;

	public EditorEntity getEditor() {
		return editor;
	}

	public void setEditor(EditorEntity editor) {
		this.editor = editor;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}
