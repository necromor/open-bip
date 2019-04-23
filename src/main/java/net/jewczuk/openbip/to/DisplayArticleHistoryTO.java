package net.jewczuk.openbip.to;

import java.util.List;

public class DisplayArticleHistoryTO {

	private String link;
	private String title;
	private List<ContentHistoryTO> contentHistory;
	private List<AttachmentHistoryTO> attachmentsHistory;

	public static class Builder {
		private String link;
		private String title;
		private List<ContentHistoryTO> contentHistory;
		private List<AttachmentHistoryTO> attachmentsHistory;

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

		public Builder contentHistory(List<ContentHistoryTO> contentHistory) {
			this.contentHistory = contentHistory;
			return this;
		}

		public Builder attachmentsHistory(List<AttachmentHistoryTO> attachmentsHistory) {
			this.attachmentsHistory = attachmentsHistory;
			return this;
		}

		public DisplayArticleHistoryTO build() {
			return new DisplayArticleHistoryTO(this);
		}
	}

	private DisplayArticleHistoryTO(Builder builder) {
		link = builder.link;
		title = builder.title;
		contentHistory = builder.contentHistory;
		attachmentsHistory = builder.attachmentsHistory;
	}

	public DisplayArticleHistoryTO() {

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

	public List<ContentHistoryTO> getContentHistory() {
		return contentHistory;
	}

	public void setContentHistory(List<ContentHistoryTO> contentHistory) {
		this.contentHistory = contentHistory;
	}

	public List<AttachmentHistoryTO> getAttachmentsHistory() {
		return attachmentsHistory;
	}

	public void setAttachmentsHistory(List<AttachmentHistoryTO> attachmentsHistory) {
		this.attachmentsHistory = attachmentsHistory;
	}

}
