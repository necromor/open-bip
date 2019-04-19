package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="blueprint")
public class BlueprintEntity extends AbstractEntity {

	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="content", nullable=false)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="editor_id")
	private EditorEntity editor;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

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
