package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.ArticleDisplayTO;
import net.jewczuk.openbip.to.ArticleEditTO;

public interface ArticleValidator {

	boolean validateAddArticle(ArticleDisplayTO article) throws BusinessException;
	
	boolean validateEditTitle(ArticleEditTO article) throws BusinessException;
}
