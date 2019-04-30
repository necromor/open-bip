package net.jewczuk.openbip.service;

import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public interface ArticleService {
	
	DisplaySingleArticleTO getArticleByLink(String link);
	
	DisplayArticleHistoryTO getHistoryByLink(String link);

}
