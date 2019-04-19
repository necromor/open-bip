package net.jewczuk.openbip.entity;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="attachment")
public class AttachmentEntity extends AbstractEntity {
	
	@Column(name="file_name", nullable=false)
	private String fileName;
	
	@Column(name="display_name", nullable=false)
	private String displayName;
	
	@Column(name="extension", nullable=false)
	private String extension;
	
	@Column(name="size", nullable=false)
	private Long size;
	
	@Column(name="display_position", nullable=false)
	private int displayPosition;
	
	@ManyToOne
	@JoinColumn(name="added_by", nullable=false)
	private EditorEntity addedBy;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="atachment_id")
	private Collection<AttachmentHistoryEntity> atachmentsHistory;

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

	public Collection<AttachmentHistoryEntity> getAtachmentsHistory() {
		if (this.atachmentsHistory == null) {
			this.atachmentsHistory = new HashSet<AttachmentHistoryEntity>();
		}
		return atachmentsHistory;
	}

	public void setAtachmentsHistory(Collection<AttachmentHistoryEntity> atachmentsHistory) {
		this.atachmentsHistory = atachmentsHistory;
	}

}
