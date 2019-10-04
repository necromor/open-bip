package net.jewczuk.openbip.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mainpage")
public class MainPageEntity extends AbstractEntity {

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "article_id")
	private ArticleEntity article;

	public MainPageEntity() {

	}

	public ArticleEntity getArticle() {
		return article;
	}

	public void setArticle(ArticleEntity article) {
		this.article = article;
	}

}
