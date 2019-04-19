package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="content_history")
public class ContentHistoryEntity extends AbstractEntity{

	@Column(name="content", nullable=false)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="editor_id", nullable=false)
	private EditorEntity editor;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public EditorEntity getEditor() {
		return editor;
	}

	public void setEditor(EditorEntity editor) {
		this.editor = editor;
	}
	
}
