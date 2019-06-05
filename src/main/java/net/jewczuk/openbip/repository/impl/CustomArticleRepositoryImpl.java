package net.jewczuk.openbip.repository.impl;

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

}
