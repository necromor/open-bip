package net.jewczuk.openbip.service;

import net.jewczuk.openbip.to.ArticleLinkTO;

public interface MainPageService {

	ArticleLinkTO getMainPage();
	
	ArticleLinkTO setMainPage(String link);
}
