package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;

public interface ArticleValidator {

	boolean validateAddArticle(DisplaySingleArticleTO article) throws BusinessException;
	
	boolean validateEditTitle(EditArticleTO article) throws BusinessException;
}
