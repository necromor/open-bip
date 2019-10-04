package net.jewczuk.openbip.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.MainPageEntity;
import net.jewczuk.openbip.mapper.ArticleMapper;
import net.jewczuk.openbip.repository.ArticleRepository;
import net.jewczuk.openbip.repository.MainPageRepository;
import net.jewczuk.openbip.service.MainPageService;
import net.jewczuk.openbip.to.ArticleLinkTO;

@Component
public class MainPageServiceImpl implements MainPageService {
	
	@Autowired
	private MainPageRepository mainPageRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleMapper articleMapper;

	@Override
	public ArticleLinkTO getMainPage() {
		return articleMapper.mapToLink(findMainPage().getArticle());	
	}

	@Override
	public ArticleLinkTO setMainPage(String link) {
		MainPageEntity mpe = findMainPage();
		ArticleEntity article = articleRepository.getArticleByLink(link);
		
		if (mpe == null) {
			mpe = new MainPageEntity();
		}
		
		mpe.setArticle(article);
		MainPageEntity saved = mainPageRepository.save(mpe);
		
		return articleMapper.mapToLink(saved.getArticle());
	}
	
	private MainPageEntity findMainPage() {
		List<MainPageEntity> mpe = mainPageRepository.findAll()
				.stream()
				.collect(Collectors.toList());
		
		if (mpe.isEmpty()) {
			return null;
		} else {
			return mpe.get(0);
		}		
	}

}
