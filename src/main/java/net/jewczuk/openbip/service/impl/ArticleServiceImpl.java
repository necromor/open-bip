package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
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

}
