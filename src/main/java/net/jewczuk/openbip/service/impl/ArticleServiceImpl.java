package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@Service
public class ArticleServiceImpl implements ArticleService {
	
	@Autowired
	private ArticleMapper articleMapper;
	
	@Autowired
	private ArticleRepository articleRepository;

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

}
