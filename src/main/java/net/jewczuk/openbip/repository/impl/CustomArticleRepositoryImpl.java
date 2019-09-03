package net.jewczuk.openbip.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.jewczuk.openbip.attributes.Positionable;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.entity.AttachmentHistoryEntity;
import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.AttachmentException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.repository.CustomArticleRepository;
import net.jewczuk.openbip.to.ArticleDisplayTO;

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

	@Override
	public ArticleEntity addContent(ArticleDisplayTO article, EditorEntity editor) {
		ArticleEntity entity = getArticleByLink(article.getLink());
		
		ContentHistoryEntity contentEntity = new ContentHistoryEntity();
		contentEntity.setContent(article.getContent());
		contentEntity.setEditor(editor);
		
		entity.getContentHistory().add(contentEntity);
		entityManager.persist(entity);
		
		return entity;
	}

	@Override
	public ArticleEntity managePinningToMainMenu(String link, boolean status) {
		ArticleEntity entity = getArticleByLink(link);
		if (entity.isMainMenu() == status) {
			return entity;
		}
		
		entity.setDisplayPosition(returnNewDisplayPosition(status));
		entity.setMainMenu(status);
		entityManager.persist(entity);
		return entity;
	}
	
	private int returnNewDisplayPosition(boolean status) {	
		return status ? getMainMenu().size() + 1 : 0;
	}

	@Override
	public ArticleEntity addAttachment(String link, AttachmentEntity attEntity, EditorEntity editor) throws BusinessException {
		ArticleEntity entity = getArticleByLink(link);
		attEntity.setAddedBy(editor);
		attEntity.setDisplayPosition(entity.getAttachments().size() + 1);
		entity.getAttachments().add(attEntity);
		entity.getAttachmentsHistory().add(createAttachmentHistory(attEntity.getDisplayName(), editor, true));
		try {
			entityManager.persist(entity);
		} catch (Exception e) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_EXISTS);
		}
		
		return entity;
	}
	
	@Override
	public ArticleEntity deleteAttachment(String link, String fileName, EditorEntity editor) throws AttachmentException {
		ArticleEntity entity = getArticleByLink(link);

		AttachmentEntity attachment;
		try {
			attachment = entityManager.createNamedQuery(AttachmentEntity.FIND_ATTACHMENT_BY_NAME, AttachmentEntity.class)
					.setParameter("fileName", fileName)
					.getSingleResult();
		} catch (Exception e) {
			throw new AttachmentException(ExceptionsMessages.ATTACHMENT_NOT_EXISTS);
		}
		
		if (entity.getAttachments().contains(attachment)) {
			entity.getAttachments().remove(attachment);
			int disPos = attachment.getDisplayPosition();
			cleanUpDisplayPositions(entity.getAttachments(), disPos);
			entityManager.remove(attachment);
					
			entity.getAttachmentsHistory().add(createAttachmentHistory(attachment.getDisplayName(), editor, false));
			entityManager.persist(entity);
		}
		
		return entity;
	}
	
	
	private AttachmentHistoryEntity createAttachmentHistory(String name, EditorEntity editor, boolean adding) {
		AttachmentHistoryEntity entity = new AttachmentHistoryEntity();
		String logMsg = adding ? LogMessages.ATTACHMENT_HISTORY_ADD : LogMessages.ATTACHMENT_HISTORY_REMOVE;
		entity.setLog(logMsg + name);
		entity.setEditor(editor);

		return entity;
	}

	private <T> void cleanUpDisplayPositions(Collection<T> elements, int dispPos) {
		for (T child : elements) {
			Positionable el = (Positionable) child;
			int oldPos = el.getDisplayPosition();
			if (oldPos > dispPos) {
				el.setDisplayPosition(oldPos - 1);
			}
		}	
	}

	@Override
	public List<ArticleEntity> getTree() {
		List<ArticleEntity> result = new ArrayList<>();
		result.addAll(getMainMenu());
		result.addAll(getUnpinnedArticles());
		result = result.stream()
				.sorted(Comparator.comparing(ArticleEntity::getTitle))
				.collect(Collectors.toList());
		
		return result;
	}

	@Override
	public ArticleEntity saveChildrenPositions(String link, List<String> children) throws BusinessException {
		ArticleEntity article = getArticleByLink(link);
		Collection<ArticleEntity> artChildren = article.getChildren();
		
		if (artChildren.size() != children.size()) {
			throw new ArticleException(ExceptionsMessages.INVALID_CHILDREN_SIZE);
		}
		
		for (ArticleEntity child: artChildren) {
			int pos = children.indexOf(child.getLink());
			if (pos == -1) {
				throw new ArticleException(ExceptionsMessages.INVALID_CHILDREN_SIZE);
			}
			child.setDisplayPosition(pos);
		}
		
		return article;
	}
	
	@Override
	public ArticleEntity saveAttachmentsPositions(String link, List<String> attachments) throws BusinessException {
		ArticleEntity article = getArticleByLink(link);
		Collection<AttachmentEntity> artAttachments = article.getAttachments();
		
		if (artAttachments.size() != attachments.size()) {
			throw new ArticleException(ExceptionsMessages.INVALID_ATTACHMENT_SIZE);
		}
		
		for (AttachmentEntity att: artAttachments) {
			int pos = attachments.indexOf(att.getFileName());
			if (pos == -1) {
				throw new ArticleException(ExceptionsMessages.INVALID_ATTACHMENT_SIZE);
			}
			att.setDisplayPosition(pos);
		}
		
		return article;
	}

	@Override
	public List<ArticleEntity> saveMenuPositions(List<String> links) throws BusinessException {
		List<ArticleEntity> newMenuPositions = new ArrayList<>();
		
		if (getMainMenu().size() != links.size()) {
			throw new ArticleException(ExceptionsMessages.INVALID_MAIN_MENU_SIZE);
		}
		
		int i = 0;
		for (String link: links) {
			ArticleEntity article = getArticleByLink(link);
			if (!article.isMainMenu()) {
				throw new ArticleException(ExceptionsMessages.NOT_IN_MAIN_MENU);
			}
			article.setDisplayPosition(i++);
			newMenuPositions.add(article);
		}
		
		return newMenuPositions;
	}

	@Override
	public boolean isReadyToBeDeleted(String link) {
		ArticleEntity article = getArticleByLink(link);

		return !(article.isMainMenu() 
				|| article.getChildren().size() > 0 
				|| article.getAttachments().size() > 0);
	}

}
