package net.jewczuk.openbip.to;

import java.time.LocalDateTime;
import java.util.List;

public class DisplaySingleArticleTO {

	private String link;
	private String title;
	private String content;
	private List<ArticleLinkTO> children;
	private LocalDateTime createdAt;
	private LocalDateTime editedAt;
	private String createdBy;
	private String editedBy;
	private List<AttachmentTO> attachments;
	private int contentChangesNumber;

	public static class Builder {
		private String link;
		private String title;
		private String content;
		private List<ArticleLinkTO> children;
		private LocalDateTime createdAt;
		private LocalDateTime editedAt;
		private String createdBy;
		private String editedBy;
		private List<AttachmentTO> attachments;
		private int contentChangesNumber;

		public Builder() {

		}

		public Builder link(String link) {
			this.link = link;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder content(String content) {
			this.content = content;
			return this;
		}

		public Builder children(List<ArticleLinkTO> children) {
			this.children = children;
			return this;
		}

		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder editedAt(LocalDateTime editedAt) {
			this.editedAt = editedAt;
			return this;
		}

		public Builder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}

		public Builder editedBy(String editedBy) {
			this.editedBy = editedBy;
			return this;
		}

		public Builder attachments(List<AttachmentTO> attachments) {
			this.attachments = attachments;
			return this;
		}
		
		public Builder contentChangesNumber(int contentChangesNumber) {
			this.contentChangesNumber = contentChangesNumber;
			return this;
		}

		public DisplaySingleArticleTO build() {
			return new DisplaySingleArticleTO(this);
		}
	}

	private DisplaySingleArticleTO(Builder builder) {
		link = builder.link;
		title = builder.title;
		content = builder.content;
		children = builder.children;
		createdAt = builder.createdAt;
		editedAt = builder.editedAt;
		createdBy = builder.createdBy;
		editedBy = builder.editedBy;
		attachments = builder.attachments;
		contentChangesNumber = builder.contentChangesNumber;
	}

	public DisplaySingleArticleTO() {

	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

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

	public List<ArticleLinkTO> getChildren() {
		return children;
	}

	public void setChildren(List<ArticleLinkTO> children) {
		this.children = children;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getEditedAt() {
		return editedAt;
	}

	public void setEditedAt(LocalDateTime editedAt) {
		this.editedAt = editedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEditedBy() {
		return editedBy;
	}

	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}

	public List<AttachmentTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentTO> attachments) {
		this.attachments = attachments;
	}

	public int getContentChangesNumber() {
		return contentChangesNumber;
	}

	public void setContentChangesNumber(int contentChangesNumber) {
		this.contentChangesNumber = contentChangesNumber;
	}

}
