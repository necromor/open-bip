package net.jewczuk.openbip.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.service.ArticleService;
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

}
