package net.jewczuk.openbip.service;

import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public interface ArticleService {
	
	DisplaySingleArticleTO getArticleByLink(String link);

}
