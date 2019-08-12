package net.jewczuk.openbip.validators.impl;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.validators.ArticleValidator;

@Component
public class ArticleValidatorImpl implements ArticleValidator {

	@Override
	public boolean validateAddArticle(DisplaySingleArticleTO article) throws BusinessException {
		if (article.getTitle() == null || article.getTitle().length() == 0) {
			throw new ArticleException(ExceptionsMessages.NO_TITLE);
		}
		
		if (article.getLink() == null || article.getLink().length() < ApplicationProperties.MIN_LINK_LENGHT) {
			throw new ArticleException(ExceptionsMessages.LINK_MIN_LENGTH);
		}
		
		return true;
	}

	@Override
	public boolean validateEditTitle(EditArticleTO article) throws BusinessException {
		if (article.getTitle() == null || article.getTitle().length() == 0) {
			throw new ArticleException(ExceptionsMessages.NO_TITLE);
		}
		
		if (article.getLink() == null || article.getLink().length() < ApplicationProperties.MIN_LINK_LENGHT) {
			throw new ArticleException(ExceptionsMessages.LINK_MIN_LENGTH);
		}
		
		return true;
	}

}
