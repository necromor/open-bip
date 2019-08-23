package net.jewczuk.openbip.to;

public class SandboxTO {

	private String title;
	private String link;
	private String content;

	public SandboxTO() {

	}

	public SandboxTO(String title, String link, String content) {
		this.title = title;
		this.link = link;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
