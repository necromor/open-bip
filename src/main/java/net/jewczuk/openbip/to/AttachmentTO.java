package net.jewczuk.openbip.to;

import java.time.LocalDateTime;

public class AttachmentTO {

	private String fileName;
	private String displayName;
	private String extension;
	private Long size;
	private String addedBy;
	private LocalDateTime addedAt;

	public static class Builder {
		private String fileName;
		private String displayName;
		private String extension;
		private Long size;
		private String addedBy;
		private LocalDateTime addedAt;

		public Builder() {

		}

		public Builder fileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public Builder displayName(String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder extension(String extension) {
			this.extension = extension;
			return this;
		}

		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		public Builder addedBy(String addedBy) {
			this.addedBy = addedBy;
			return this;
		}

		public Builder addedAt(LocalDateTime addedAt) {
			this.addedAt = addedAt;
			return this;
		}

		public AttachmentTO build() {
			return new AttachmentTO(this);
		}
	}

	private AttachmentTO(Builder builder) {
		fileName = builder.fileName;
		displayName = builder.displayName;
		extension = builder.extension;
		size = builder.size;
		addedBy = builder.addedBy;
		addedAt = builder.addedAt;
	}

	public AttachmentTO() {

	}

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

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public LocalDateTime getAddedAt() {
		return addedAt;
	}

	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}

}
