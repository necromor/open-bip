package net.jewczuk.openbip.service;

import java.util.List;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.AttachmentTO;
import net.jewczuk.openbip.to.ArticleHistoryTO;
import net.jewczuk.openbip.to.ArticleDisplayTO;
import net.jewczuk.openbip.to.ArticleEditTO;
import net.jewczuk.openbip.to.TreeBranchTO;

public interface ArticleService {
	
	ArticleDisplayTO getArticleByLink(String link);
	
	ArticleHistoryTO getHistoryByLink(String link);
	
	List<ArticleLinkTO> getMainMenu();
	
	List<ArticleLinkTO> getBreadcrumbs(String link);
	
	List<ArticleLinkTO> getAllArticles();
	
	List<TreeBranchTO> getTree();

	ArticleDisplayTO saveArticle(ArticleDisplayTO article, Long editorID)
			throws BusinessException;
	
	ArticleEditTO getArticleByLinkToEdit(String link);

	ArticleEditTO editTitle(ArticleEditTO article, Long editorID) 
			throws BusinessException;
	
	List<ArticleLinkTO> getAllUnpinnedArticles();
	
	ArticleLinkTO managePinningToMainMenu(String link, Long editorID, boolean status) 
			throws BusinessException;

	ArticleDisplayTO editContent(ArticleDisplayTO article, Long editorID) 
			throws BusinessException;

	ArticleDisplayTO managePinningChildren(String parent, String child, Long editorID, boolean status) 
			throws BusinessException;
	
	ArticleDisplayTO addAttachment(String link, AttachmentTO attachment, Long editorID) 
			throws BusinessException;

	ArticleDisplayTO deleteAttachment(String link, String fileName, Long editorID) 
			throws BusinessException;

	ArticleDisplayTO saveChildrenPositions(String link, String[] children, Long editorID) 
			throws BusinessException;

	ArticleDisplayTO saveAttachmentsPositions(String link, String[] attachments, Long editorID)
			throws BusinessException;

	List<ArticleLinkTO> saveMenuPositions(String[] links, Long editorID) 
			throws BusinessException;

	void deleteArticle(String link, Long editorID) 
			throws BusinessException;
	
}
