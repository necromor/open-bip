package net.jewczuk.openbip.to;

public class ArticleEditTO {

	private String link;
	private String title;
	private String oldLink;

	public static class Builder {
		private String link;
		private String title;
		private String oldLink;

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

		public Builder oldLink(String oldLink) {
			this.oldLink = oldLink;
			return this;
		}

		public ArticleEditTO build() {
			return new ArticleEditTO(this);
		}

	}

	public ArticleEditTO() {

	}

	private ArticleEditTO(Builder builder) {
		link = builder.link;
		title = builder.title;
		oldLink = builder.oldLink;
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

	public String getOldLink() {
		return oldLink;
	}

	public void setOldLink(String oldLink) {
		this.oldLink = oldLink;
	}

}
