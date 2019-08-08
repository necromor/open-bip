package net.jewczuk.openbip.validators;

import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

public interface ArticleValidator {

	boolean validateAddArticle(DisplaySingleArticleTO article) throws BusinessException;
}
