package net.jewczuk.openbip.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.repository.CustomArticleRepository;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public class CustomArticleRepositoryImpl 
	implements CustomArticleRepository {
	
	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public ArticleEntity getArticleByLink(String link) {
		
		ArticleEntity entity;
		try {
			entity = entityManager.createNamedQuery(ArticleEntity.FIND_SINGLE_ARTICLE_BY_LINK, ArticleEntity.class)
					.setParameter("link", link)
					.getSingleResult();
		} catch (Exception empty) {
			throw new ResourceNotFoundException();
		}
		
		return entity;
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

	@Override
	public List<ArticleEntity> managePinningChild(String parent, String child) throws BusinessException {
		
		ArticleEntity parentEntity = getArticleByLink(parent);
		ArticleEntity childEntity = getArticleByLink(child);
		
		validateChild(childEntity, parentEntity);
		
		if (parentEntity.getChildren().contains(childEntity)) {
			int dispPos = childEntity.getDisplayPosition();
			childEntity.setDisplayPosition(0);
			parentEntity.getChildren().remove(childEntity);
			cleanUpDisplayPositions(parentEntity.getChildren(), dispPos);
		} else {
			childEntity.setDisplayPosition(parentEntity.getChildren().size() + 1);
			parentEntity.getChildren().add(childEntity);
		}
		
		entityManager.persist(parentEntity);
		
		List<ArticleEntity> result = new ArrayList<>();
		result.add(parentEntity);
		result.add(childEntity);

		return result;
	}

	private void validateChild(ArticleEntity childEntity, ArticleEntity parentEntity) throws BusinessException {
		
		if (childEntity.getLink().equals(parentEntity.getLink())) {
			throw new ArticleException(ExceptionsMessages.PINNING_TO_SELF);
		}
		
		if (childEntity.isMainMenu()) {
			throw new ArticleException(ExceptionsMessages.PINNED_TO_ANOTHER);
		}
		
		ArticleEntity childParent = getParentArticle(childEntity.getLink());
		if (childParent != null && childParent.getId() != parentEntity.getId()) {
			throw new ArticleException(ExceptionsMessages.PINNED_TO_ANOTHER);
		}
	}

	private void cleanUpDisplayPositions(Collection<ArticleEntity> children, int dispPos) {
		for (ArticleEntity child : children) {
			int oldPos = child.getDisplayPosition();
			if (oldPos > dispPos) {
				child.setDisplayPosition(oldPos - 1);
			}
		}	
	}

	@Override
	public ArticleEntity addContent(DisplaySingleArticleTO article, EditorEntity editor) {
		ArticleEntity entity = getArticleByLink(article.getLink());
		
		ContentHistoryEntity contentEntity = new ContentHistoryEntity();
		contentEntity.setContent(article.getContent());
		contentEntity.setEditor(editor);
		
		entity.getContentHistory().add(contentEntity);
		entityManager.persist(entity);
		
		return entity;
	}

}
