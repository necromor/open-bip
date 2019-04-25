package net.jewczuk.openbip.repository;

import net.jewczuk.openbip.entity.ArticleEntity;

public interface CustomArticleRepository {
	
	ArticleEntity getArticleByLink(String link);

}
