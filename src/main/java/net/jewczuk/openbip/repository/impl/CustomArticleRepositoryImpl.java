package net.jewczuk.openbip.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.repository.CustomArticleRepository;

public class CustomArticleRepositoryImpl 
	implements CustomArticleRepository {
	
	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public ArticleEntity getArticleByLink(String link) {
		return entityManager.createNamedQuery(ArticleEntity.FIND_SINGLE_ARTICLE_BY_LINK, ArticleEntity.class)
				.setParameter("link", link)
				.getSingleResult();
	}

	@Override
	public List<ArticleEntity> getMainMenu() {
		return entityManager.createNamedQuery(ArticleEntity.FIND_MAIN_MENU, ArticleEntity.class)
				.getResultList();
	}

	@Override
	public List<ArticleEntity> getBreadcrumbs(String link) {
		List<ArticleEntity> breadCrumbs = new ArrayList<>();
		ArticleEntity parent = getParentArticle(link);
		while (parent != null) {
			breadCrumbs.add(parent);
			parent = getParentArticle(parent.getLink());
		}

		return breadCrumbs;
	}

	@Override
	public ArticleEntity getParentArticle(String link) {
		ArticleEntity article;
		try {
			article = entityManager.createNamedQuery(ArticleEntity.FIND_PARENT, ArticleEntity.class)
					.setParameter("link", link)
					.getSingleResult();
		} catch (Exception e) {
			article = null;
		}
		
		return article;
	}

	@Override
	public List<ArticleEntity> getUnpinnedArticles() {
		return entityManager.createNamedQuery(ArticleEntity.FIND_UNPINNED_ARTICLES, ArticleEntity.class)
				.getResultList();
	}

}
