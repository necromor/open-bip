package net.jewczuk.openbip.to;

import java.util.List;

public class TreeBranchTO {

	private String link;
	private String title;
	private boolean published;
	private List<TreeBranchTO> children;

	public static class Builder {
		private String link;
		private String title;
		private boolean published;
		private List<TreeBranchTO> children;

		public Builder link(String link) {
			this.link = link;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder published(boolean published) {
			this.published = published;
			return this;
		}

		public Builder children(List<TreeBranchTO> children) {
			this.children = children;
			return this;
		}

		public TreeBranchTO build() {
			return new TreeBranchTO(this);
		}

	}

	public TreeBranchTO() {

	}

	private TreeBranchTO(Builder builder) {
		link = builder.link;
		title = builder.title;
		published = builder.published;
		children = builder.children;
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<TreeBranchTO> getChildren() {
		return children;
	}

	public void setChildren(List<TreeBranchTO> children) {
		this.children = children;
	}

}
