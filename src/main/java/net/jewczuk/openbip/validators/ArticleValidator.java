package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.ArticleEditTO;

public interface ArticleValidator {

	boolean validateAddArticle(DisplaySingleArticleTO article) throws BusinessException;
	
	boolean validateEditTitle(ArticleEditTO article) throws BusinessException;
}
