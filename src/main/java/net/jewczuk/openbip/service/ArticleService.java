package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public interface ArticleService {
	
	DisplaySingleArticleTO getArticleByLink(String link);
	
	DisplayArticleHistoryTO getHistoryByLink(String link);
	
	List<ArticleLinkTO> getMainMenu();
	
	List<ArticleLinkTO> getBreadcrumbs(String link);
	
	List<ArticleLinkTO> getAllArticles();

	DisplaySingleArticleTO saveArticle(DisplaySingleArticleTO article, Long editorID) throws BusinessException;
}
