package net.jewczuk.openbip.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ArticleServiceImplTest {

	@Autowired
	private ArticleService articleService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenArticleLinkIsInvalid() {
		
		excE.expect(EmptyResultDataAccessException.class);
		articleService.getArticleByLink(TestConstants.INVALID_LINK);
	}
	
	@Test
	public void shouldReturnArticleWithoutAttachmentsNoChildrenAndNoHistory() {
		DisplaySingleArticleTO article = articleService.getArticleByLink(TestConstants.CHILD_2_1_LINK);
		
		assertThat(article.getTitle()).isEqualTo(TestConstants.CHILD_2_1_TITLE);
		assertThat(article.getAttachments()).isEmpty();
		assertThat(article.getChildren()).isEmpty();
		assertThat(article.getCreatedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(article.getEditedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(article.getCreatedAt()).isEqualTo(article.getEditedAt());
		assertThat(article.getContentChangesNumber()).isEqualTo(0);
	}
	
	@Test
	public void shouldReturnArticleWithContentHistory() {
		DisplaySingleArticleTO article = articleService.getArticleByLink(TestConstants.CHILD_1_LINK);
		
		assertThat(article.getTitle()).isEqualTo(TestConstants.CHILD_1_TITLE);
		assertThat(article.getContent()).isEqualTo(TestConstants.CHILD_1_CONTENT);
		assertThat(article.getCreatedBy()).isEqualTo(TestConstants.EDITOR_1);
		assertThat(article.getEditedBy()).isEqualTo(TestConstants.EDITOR_1);
		assertThat(article.getCreatedAt()).isNotEqualTo(article.getEditedAt());
		assertThat(article.getContentChangesNumber()).isEqualTo(1);
	}
	
	@Test
	public void shouldReturArticleWithChildrenAndAttachments() {
		List<ArticleLinkTO> children = new ArrayList<>();
		children.add(new ArticleLinkTO(TestConstants.CHILD_2_LINK, TestConstants.CHILD_2_TITLE));
		children.add(new ArticleLinkTO(TestConstants.CHILD_1_LINK, TestConstants.CHILD_1_TITLE));
		List<String> expectedAttachments = Arrays.asList(TestConstants.ATTACHMENT_2_NAME, TestConstants.ATTACHMENT_1_NAME);
		
		DisplaySingleArticleTO article = articleService.getArticleByLink(TestConstants.PARENT_LINK);
		List<String> actualAttachments = article.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(article.getContent()).isEqualTo(TestConstants.EMPTY_CONTENT);
		assertThat(article.getChildren()).containsExactlyElementsOf(children);
		assertThat(actualAttachments).containsExactlyElementsOf(expectedAttachments);
	}
	
	@Test
	public void shouldReturnArticleWithContentHistoryFromDifferentEditors() {
		DisplaySingleArticleTO article = articleService.getArticleByLink(TestConstants.NO_CHILDREN_LINK);
		
		assertThat(article.getTitle()).isEqualTo(TestConstants.NO_CHILDREN_TITLE);
		assertThat(article.getContent()).isEqualTo(TestConstants.NO_CHILDREN_CONTENT);
		assertThat(article.getCreatedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(article.getEditedBy()).isEqualTo(TestConstants.EDITOR_1);
		assertThat(article.getAttachments().size()).isEqualTo(3);
		assertThat(article.getAttachments()).allMatch(a -> a.getExtension().equals("odt"));
		assertThat(article.getContentChangesNumber()).isEqualTo(2);
	}
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenHistoryLinkIsInvalid() {
		
		excE.expect(EmptyResultDataAccessException.class);
		articleService.getHistoryByLink(TestConstants.INVALID_LINK);
	}
	
	@Test
	public void shouldReturnHistoryWhenThereWereNoChangesInContentOrAttachments() {
		DisplayArticleHistoryTO history = articleService.getHistoryByLink(TestConstants.CHILD_2_LINK);
		
		assertThat(history.getAttachmentsHistory()).isEmpty();
		assertThat(history.getContentHistory().size()).isEqualTo(1);
		assertThat(history.getTitle()).isEqualTo(TestConstants.CHILD_2_TITLE);
		assertThat(history.getContentHistory().get(0).getContent()).isEqualTo(TestConstants.CHILD_2_CONTENT);
		assertThat(history.getContentHistory().get(0).getCreatedBy()).isEqualTo(TestConstants.EDITOR_3);
	}
	
	@Test
	public void shouldReturnSortedHistory() {
		DisplayArticleHistoryTO history = articleService.getHistoryByLink(TestConstants.CHILD_1_LINK);
		List<String> contentList = history.getContentHistory().stream()
				.map(ch -> ch.getContent())
				.collect(Collectors.toList());
		
		assertThat(contentList).containsExactly(TestConstants.CHILD_1_CONTENT, TestConstants.CHILD_1_CONTENT_OLD);
		assertThat(history.getContentHistory()).allMatch(ch -> ch.getCreatedBy().equals(TestConstants.EDITOR_1));
	}
	
	@Test
	public void shouldReturnSortedAttachmentsHistory() {
		DisplayArticleHistoryTO history = articleService.getHistoryByLink(TestConstants.NO_CHILDREN_LINK);
		
		assertThat(history.getAttachmentsHistory().size()).isEqualTo(5);
		assertThat(history.getAttachmentsHistory().get(0).getCreatedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(history.getAttachmentsHistory().get(4).getCreatedBy()).isEqualTo(TestConstants.EDITOR_1);
		assertThat(history.getAttachmentsHistory().get(1).getLog()).isEqualTo(TestConstants.LOG_3_TEXT);
		assertThat(history.getAttachmentsHistory().get(3).getLog()).isEqualTo(TestConstants.LOG_2_TEXT);
	}
	
	@Test
	public void shouldReturnMainMenu() {
		List<ArticleLinkTO> mainMenu = articleService.getMainMenu();
		List<String> titles = mainMenu.stream().map(a -> a.getTitle()).collect(Collectors.toList());
		List<String> links = mainMenu.stream().map(a -> a.getLink()).collect(Collectors.toList());
		
		assertThat(titles).containsExactly(TestConstants.MAIN_PAGE_TITLE, TestConstants.NO_CHILDREN_TITLE, TestConstants.PARENT_TITLE);
		assertThat(links).containsExactly(TestConstants.MAIN_PAGE_LINK, TestConstants.NO_CHILDREN_LINK, TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldReturnEmptyListWhenArticleIsOnLevel1() {
		List<ArticleLinkTO> breadCrumbs = articleService.getBreadcrumbs(TestConstants.NO_CHILDREN_LINK);
		
		assertThat(breadCrumbs).isEmpty();
	}
	
	@Test
	public void shouldReturnOneElementWhenArticleIsOnLevel2() {
		List<ArticleLinkTO> breadCrumbs = articleService.getBreadcrumbs(TestConstants.CHILD_2_LINK);
		
		assertThat(breadCrumbs.size()).isEqualTo(1);
		assertThat(breadCrumbs.get(0).getLink()).isEqualTo(TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldReturnTwoElementsWhenArticleIsOnLevel3() {
		List<ArticleLinkTO> breadCrumbs = articleService.getBreadcrumbs(TestConstants.CHILD_2_1_LINK);
		
		assertThat(breadCrumbs.size()).isEqualTo(2);
		assertThat(breadCrumbs.get(0).getLink()).isEqualTo(TestConstants.CHILD_2_LINK);
		assertThat(breadCrumbs.get(1).getLink()).isEqualTo(TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldReturnAllArticlesLinksSortedByTitleASC() {
		List<ArticleLinkTO> articles = articleService.getAllArticles();
		
		assertThat(articles.size()).isEqualTo(8);
		assertThat(articles.get(1).getTitle()).isEqualTo(TestConstants.NO_CHILDREN_TITLE);
		assertThat(articles.get(5).getLink()).isEqualTo(TestConstants.MAIN_PAGE_LINK);
	}
	
}
 