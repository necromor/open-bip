package net.jewczuk.openbip.to;

public class ArticleLinkTO {

	private String link;
	private String Title;

	public ArticleLinkTO() {

	}

	public ArticleLinkTO(String link, String title) {
		super();
		this.link = link;
		Title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

}
