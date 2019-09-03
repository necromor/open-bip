package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.mapper.AttachmentMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.AttachmentTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.to.TreeBranchTO;
import net.jewczuk.openbip.utils.TransformUtils;
import net.jewczuk.openbip.validators.ArticleValidator;

@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleValidator articleValidator;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private AttachmentMapper attachmentMapper;

	@Override
	public DisplaySingleArticleTO getArticleByLink(String link) {
		return articleMapper.mapToDisplaySingleArticle(articleRepository.getArticleByLink(link));
	}

	@Override
	public DisplayArticleHistoryTO getHistoryByLink(String link) {
		return articleMapper.mapToHistory(articleRepository.getArticleByLink(link));
	}

	@Override
	public List<ArticleLinkTO> getMainMenu() {
		return articleRepository.getMainMenu().stream()
				.map(a -> articleMapper.mapToLink(a))
				.collect(Collectors.toList());
	}

	@Override
	public List<ArticleLinkTO> getBreadcrumbs(String link) {
		return articleRepository.getBreadcrumbs(link).stream()
				.map(a -> articleMapper.mapToLink(a))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ArticleLinkTO> getAllArticles() {
		return articleRepository.findAllByOrderByTitleAsc().stream()
				.map(a -> articleMapper.mapToLink(a))
				.collect(Collectors.toList());
	}

	@Override
	public DisplaySingleArticleTO saveArticle(DisplaySingleArticleTO article, Long editorID) throws BusinessException {
		
		ArticleEntity entity = new ArticleEntity();
		
		try {
			articleValidator.validateAddArticle(article);
			entity = articleRepository.save(articleMapper.map2NewAE(article));
			historyService.createLogEntry(LogMessages.ARTICLE_ADDED + article.getTitle(), editorID);
		} catch (BusinessException be) {
			throw new ArticleException(be.getMessage());
		} catch (Exception e) {
			throw new ArticleException(ExceptionsMessages.LINK_EXISTS);
		}

		return articleMapper.mapToDisplaySingleArticle(entity);
	}

	@Override
	public EditArticleTO getArticleByLinkToEdit(String link) {
		DisplaySingleArticleTO aTO = getArticleByLink(link);
		return new EditArticleTO.Builder()
				.link(aTO.getLink())
				.title(aTO.getTitle())
				.oldLink(aTO.getLink())
				.build();
	}

	@Override
	public EditArticleTO editTitle(EditArticleTO article, Long editorID) throws BusinessException {

		ArticleEntity entity = articleRepository.getArticleByLink(article.getOldLink());

		entity.setTitle(article.getTitle());
		entity.setLink(article.getLink());
		
		try {
			articleValidator.validateEditTitle(article);
			entity = articleRepository.saveAndFlush(entity);
			historyService.createLogEntry(LogMessages.ARTICLE_TITLE_EDITED + article.getTitle(), editorID);
		} catch (BusinessException be) {
			throw new ArticleException(be.getMessage());
		} catch (Exception e) {
			throw new ArticleException(ExceptionsMessages.LINK_EXISTS);
		}
		
		return articleMapper.map2EditArticle(entity);
	}

	@Override
	public List<ArticleLinkTO> getAllUnpinnedArticles() {
		return articleRepository.getUnpinnedArticles().stream()
				.map(a -> articleMapper.mapToLink(a))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ArticleLinkTO managePinningToMainMenu(String link, Long editorID, boolean status) throws BusinessException {
		ArticleEntity entity = articleRepository.managePinningToMainMenu(link, status);
			
		String logMessage = status ? LogMessages.ARTICLE_PINNED_TO_MAIN_MENU : LogMessages.ARTICLE_UNPINNED_TO_MAIN_MENU;
		historyService.createLogEntry(logMessage + entity.getTitle(), editorID);
		
		return articleMapper.mapToLink(entity);
	}

	@Override
	@Transactional
	public DisplaySingleArticleTO editContent(DisplaySingleArticleTO article, Long editorID) throws BusinessException {
		
		EditorEntity editor = editorRepository.getEditorById(editorID);
		ArticleEntity entity = articleRepository.addContent(article, editor);
			
		historyService.createLogEntry(LogMessages.ARTICLE_CONTENT_EDITED + entity.getTitle(), editorID);
		
		return articleMapper.mapToDisplaySingleArticle(entity);
	}

	@Override
	@Transactional
	public DisplaySingleArticleTO managePinningChildren(String parent, String child, Long editorID, boolean status)
			throws BusinessException {
		
		List<ArticleEntity> entities = articleRepository.managePinningChild(parent, child);
		
		String logMessage = entities.get(0).getTitle();
		logMessage += status ? LogMessages.ARTICLE_PINNED_CHILD : LogMessages.ARTICLE_UNPINNED_CHILD;
		logMessage += entities.get(1).getTitle();
		
		historyService.createLogEntry(logMessage, editorID);
		
		return articleMapper.mapToDisplaySingleArticle(entities.get(0));
	}

	@Override
	@Transactional
	public DisplaySingleArticleTO addAttachment(String link, AttachmentTO attachment, Long editorID)
			throws BusinessException {
		
		AttachmentEntity attEntity = attachmentMapper.mapToNewEntity(attachment);
		EditorEntity editor = editorRepository.getEditorById(editorID);
		ArticleEntity article = articleRepository.addAttachment(link, attEntity, editor);
		
		String logMessage = article.getTitle() + LogMessages.ARTICLE_ADD_ATTACHMENT + attEntity.getFileName();
		historyService.createLogEntry(logMessage, editorID);	
		
		return articleMapper.mapToDisplaySingleArticle(article);
	}

	@Override
	@Transactional
	public DisplaySingleArticleTO deleteAttachment(String link, String fileName, Long editorID) throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(editorID);
		ArticleEntity article = articleRepository.deleteAttachment(link, fileName, editor);
		
		String logMessage = article.getTitle() + LogMessages.ARTICLE_DELETE_ATTACHMENT + fileName;
		historyService.createLogEntry(logMessage, editorID);	
		
		return articleMapper.mapToDisplaySingleArticle(article);
	}

	@Override
	public List<TreeBranchTO> getTree() {
		return articleRepository.getTree().stream()
				.map(a -> articleMapper.mapToTreeBranch(a))
				.collect(Collectors.toList());
	}

	@Override
	public DisplaySingleArticleTO saveChildrenPositions(String link, String[] children, Long editorID)
			throws BusinessException {
		List<String> links = TransformUtils.crateListFromArray(children);
		ArticleEntity article = articleRepository.saveChildrenPositions(link, links);
		
		String logMessage = LogMessages.ARTICLE_CHILDREN_POSITIONS + article.getTitle() ;
		historyService.createLogEntry(logMessage, editorID);	
		
		return articleMapper.mapToDisplaySingleArticle(article);
	}
	
	@Override
	public DisplaySingleArticleTO saveAttachmentsPositions(String link, String[] attachments, Long editorID)
			throws BusinessException {
		List<String> fileNames = TransformUtils.crateListFromArray(attachments);
		ArticleEntity article = articleRepository.saveAttachmentsPositions(link, fileNames);
		
		String logMessage = LogMessages.ARTICLE_ATTACHMENTS_POSITIONS + article.getTitle();
		historyService.createLogEntry(logMessage, editorID);	
		
		return articleMapper.mapToDisplaySingleArticle(article);
	}
	
	@Override
	public List<ArticleLinkTO> saveMenuPositions(String[] links, Long editorID) throws BusinessException {
		List<String> mainMenu = TransformUtils.crateListFromArray(links);	
		List<ArticleEntity> newMainMenu = articleRepository.saveMenuPositions(mainMenu);
		
		historyService.createLogEntry(LogMessages.MAIN_MENU_POSITIONS, editorID);
		
		return newMainMenu.stream()
				.map(a -> articleMapper.mapToLink(a))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteArticle(String link, Long editorID) throws BusinessException {
		
		if (articleRepository.isReadyToBeDeleted(link)) {
			ArticleEntity article = articleRepository.getArticleByLink(link);
			articleRepository.delete(article);
			
			String logMessage = LogMessages.ARTICLE_DELETED + article.getTitle();
			historyService.createLogEntry(logMessage, editorID);			
		} else {
			throw new ArticleException(ExceptionsMessages.ARTICLE_NOT_READY_TO_BE_DELETED);	
		}
	}

}
