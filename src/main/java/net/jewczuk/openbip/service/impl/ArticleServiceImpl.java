package net.jewczuk.openbip.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.repository.EditorRepository;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
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
	public ArticleLinkTO managePinningToMainMenu(String link, Long editorID, boolean status) {

		ArticleEntity entity = articleRepository.getArticleByLink(link);

		try {
			entity.setMainMenu(status);
			entity.setDisplayPosition(returnNewDisplayPosition(status));
			entity = articleRepository.saveAndFlush(entity);
			
			String logMessage = status ? LogMessages.ARTICLE_PINNED_TO_MAIN_MENU : LogMessages.ARTICLE_UNPINNED_TO_MAIN_MENU;
			historyService.createLogEntry(logMessage + entity.getTitle(), editorID);
		} catch (BusinessException e) {
			//omitting invalid editorID exception that can be created during logging
		}
		
		return articleMapper.mapToLink(entity);
	}
	
	private int returnNewDisplayPosition(boolean status) {
		
		return status ? getMainMenu().size() + 1 : 0;
	}

	@Override
	public DisplaySingleArticleTO editContent(DisplaySingleArticleTO article, Long editorID) throws BusinessException {
	
		ArticleEntity entity = articleRepository.getArticleByLink(article.getLink());
		
		ContentHistoryEntity contentEntity = new ContentHistoryEntity();
		contentEntity.setContent(article.getContent());
		
		Optional<EditorEntity> editorO = editorRepository.findById(editorID);
		if (editorO.isPresent()) {
			contentEntity.setEditor(editorO.get());
		} else {
			throw new EditorException(ExceptionsMessages.INVALID_EDITOR_ID);
		}
		
		Collection<ContentHistoryEntity> history = entity.getContentHistory();
		history.add(contentEntity);
		entity.setContentHistory(history);
		
		try {
			entity = articleRepository.saveAndFlush(entity);
			
			historyService.createLogEntry(LogMessages.ARTICLE_CONTENT_EDITED + entity.getTitle(), editorID);
		} catch (BusinessException e) {
			// omitting invalid editorID exception that can be created during logging
			// editor is valid
		}
		
		return articleMapper.mapToDisplaySingleArticle(entity);
	}

}
