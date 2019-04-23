package net.jewczuk.openbip.to;

import java.time.LocalDateTime;

public class AttachmentHistoryTO {

	private String log;
	private String createdBy;
	private LocalDateTime createdAt;

	public static class Builder {
		private String log;
		private String createdBy;
		private LocalDateTime createdAt;

		public Builder() {

		}

		public Builder log(String log) {
			this.log = log;
			return this;
		}

		public Builder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public AttachmentHistoryTO build() {
			return new AttachmentHistoryTO(this);
		}
	}

	private AttachmentHistoryTO(Builder builder) {
		log = builder.log;
		createdBy = builder.createdBy;
		createdAt = builder.createdAt;
	}

	public AttachmentHistoryTO() {

	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
