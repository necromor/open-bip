package net.jewczuk.openbip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = AttachmentEntity.FIND_ATTACHMENT_BY_NAME, query = "SELECT att FROM AttachmentEntity att "
			+ " WHERE att.fileName = :fileName")
	})
@Entity
@Table(name = "attachment")
public class AttachmentEntity extends AbstractEntity {
	
	public static final String FIND_ATTACHMENT_BY_NAME = "findAttachmentByName";

	@Column(name = "file_name", nullable = false, unique = true)
	private String fileName;

	@Column(name = "display_name", nullable = false)
	private String displayName;

	@Column(name = "extension", nullable = false)
	private String extension;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name = "display_position", nullable = false)
	private int displayPosition;

	@ManyToOne
	@JoinColumn(name = "added_by", nullable = false)
	private EditorEntity addedBy;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public int getDisplayPosition() {
		return displayPosition;
	}

	public void setDisplayPosition(int displayPosition) {
		this.displayPosition = displayPosition;
	}

	public EditorEntity getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(EditorEntity addedBy) {
		this.addedBy = addedBy;
	}

}
