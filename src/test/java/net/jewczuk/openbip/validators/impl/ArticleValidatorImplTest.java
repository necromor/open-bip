package net.jewczuk.openbip.validators.impl;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.validators.ArticleValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleValidatorImplTest {
	
	@Autowired
	private ArticleValidator articleValidator;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	DisplaySingleArticleTO article;
	
	@Before
	public void setUp() {
		article = new DisplaySingleArticleTO();
	}
	
	@Test
	public void shouldThrowArticleExceptionWhenTitleIsNull() throws BusinessException {
		article.setTitle(null);
		article.setLink(null);
		 
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.NO_TITLE);
		articleValidator.validateAddArticle(article);
	}
	
	@Test
	public void shouldThrowArticleExceptionWhenTitleIsEmptyString() throws BusinessException {
		article.setTitle("");
		article.setLink(null);
		 
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.NO_TITLE);
		articleValidator.validateAddArticle(article);
	}
	
	@Test
	public void shouldThrowArticleExceptionWhenLinkIsNull() throws BusinessException {
		article.setTitle(TestConstants.PARENT_TITLE);
		article.setLink(null);
		 
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.LINK_MIN_LENGTH);
		articleValidator.validateAddArticle(article);
	}

	@Test
	public void shouldThrowArticleExceptionWhenLinkIsToShort() throws BusinessException {
		article.setTitle(TestConstants.PARENT_TITLE);
		article.setLink("aa");
		 
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.LINK_MIN_LENGTH);
		articleValidator.validateAddArticle(article);
	}
	
	@Test
	public void shouldReturnTrueWhenTitleExistsAndLinkLenghtIsOk() throws BusinessException {
		article.setTitle(TestConstants.PARENT_TITLE);
		article.setLink(TestConstants.PARENT_LINK);
		
		boolean result = articleValidator.validateAddArticle(article);
		
		assertThat(result).isTrue();		
	}
	
	
}
