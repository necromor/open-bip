package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.AttachmentTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.to.TreeBranchTO;

public interface ArticleService {
	
	DisplaySingleArticleTO getArticleByLink(String link);
	
	DisplayArticleHistoryTO getHistoryByLink(String link);
	
	List<ArticleLinkTO> getMainMenu();
	
	List<ArticleLinkTO> getBreadcrumbs(String link);
	
	List<ArticleLinkTO> getAllArticles();
	
	List<TreeBranchTO> getTree();

	DisplaySingleArticleTO saveArticle(DisplaySingleArticleTO article, Long editorID) throws BusinessException;
	
	EditArticleTO getArticleByLinkToEdit(String link);

	EditArticleTO editTitle(EditArticleTO article, Long editorID) throws BusinessException;
	
	List<ArticleLinkTO> getAllUnpinnedArticles();
	
	ArticleLinkTO managePinningToMainMenu(String link, Long editorID, boolean status) throws BusinessException;

	DisplaySingleArticleTO editContent(DisplaySingleArticleTO article, Long editorID) throws BusinessException;

	DisplaySingleArticleTO managePinningChildren(String parent, String child, Long editorID, boolean status) throws BusinessException;
	
	DisplaySingleArticleTO addAttachment(String link, AttachmentTO attachment, Long editorID) throws BusinessException;

	DisplaySingleArticleTO deleteAttachment(String link, String fileName, Long editorID) throws BusinessException;
	
}
