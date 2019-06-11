package net.jewczuk.openbip.repository;

import java.util.List;

import net.jewczuk.openbip.entity.ArticleEntity;

public interface CustomArticleRepository {
	
	ArticleEntity getArticleByLink(String link);
	
	List<ArticleEntity> getMainMenu();
	
	List<ArticleEntity> getBreadcrumbs(String link);
	
	ArticleEntity getParentArticle(String link);

}
