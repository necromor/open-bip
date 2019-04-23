package net.jewczuk.openbip.to;

import java.time.LocalDateTime;

public class ContentHistoryTO {

	private String content;
	private String createdBy;
	private LocalDateTime createdAt;

	public static class Builder {
		private String content;
		private String createdBy;
		private LocalDateTime createdAt;

		public Builder() {

		}

		public Builder content(String content) {
			this.content = content;
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

		public ContentHistoryTO build() {
			return new ContentHistoryTO(this);
		}
	}

	private ContentHistoryTO(Builder builder) {
		content = builder.content;
		createdBy = builder.createdBy;
		createdAt = builder.createdAt;
	}

	public ContentHistoryTO() {

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
