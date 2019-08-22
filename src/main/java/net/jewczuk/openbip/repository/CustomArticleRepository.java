package net.jewczuk.openbip.repository;

import java.util.List;

import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public interface CustomArticleRepository {
	
	ArticleEntity getArticleByLink(String link);
	
	List<ArticleEntity> getMainMenu();
	
	List<ArticleEntity> getBreadcrumbs(String link);
	
	ArticleEntity getParentArticle(String link);
	
	List<ArticleEntity> getUnpinnedArticles();
	
	List<ArticleEntity> managePinningChild(String parent, String child) throws BusinessException;
	
	ArticleEntity addContent(DisplaySingleArticleTO article, EditorEntity editor);
	
	ArticleEntity managePinningToMainMenu(String link, boolean status);
	
	ArticleEntity addAttachment(String link, AttachmentEntity attEntity, EditorEntity editor) throws BusinessException;

	ArticleEntity deleteAttachment(String link, String fileName, EditorEntity editor) throws BusinessException;
}
