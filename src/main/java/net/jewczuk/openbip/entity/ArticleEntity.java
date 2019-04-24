package net.jewczuk.openbip.entity;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="article")
public class ArticleEntity extends AbstractEntity{

	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="link", nullable=false, unique=true)
	private String link;
	
	@Column(name="display_position", nullable=false)
	private int displayPosition;
	
	@Column(name="main_menu")
	private boolean mainMenu;
	
	@OneToMany
	@JoinColumn(name="parent_id")
	private Collection<ArticleEntity> children;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="article_id")
	private Collection<ContentHistoryEntity> contentHistory;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="article_id")
	private Collection<AttachmentEntity> attachments;

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

	public int getDisplayPosition() {
		return displayPosition;
	}

	public void setDisplayPosition(int displayPosition) {
		this.displayPosition = displayPosition;
	}

	public boolean isMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(boolean mainMenu) {
		this.mainMenu = mainMenu;
	}

	public Collection<ArticleEntity> getChildren() {
		return children;
	}

	public void setChildren(Collection<ArticleEntity> children) {
		this.children = children;
	}

	public Collection<ContentHistoryEntity> getContentHistory() {
		if (this.contentHistory == null) {
			this.contentHistory = new HashSet<ContentHistoryEntity>();
		}
		return contentHistory;
	}

	public void setContentHistory(Collection<ContentHistoryEntity> contentHistory) {
		this.contentHistory = contentHistory;
	}

	public Collection<AttachmentEntity> getAttachments() {
		if (this.attachments == null) {
			this.attachments = new HashSet<AttachmentEntity>();
		}
		return attachments;
	}

	public void setAttachments(Collection<AttachmentEntity> attachments) {
		this.attachments = attachments;
	}
	
}
 