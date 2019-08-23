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
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.AttachmentException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.EditorException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.service.HistoryService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.AttachmentTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;
import net.jewczuk.openbip.to.EditArticleTO;
import net.jewczuk.openbip.to.HistoryTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ArticleServiceImplTest {

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private HistoryService historyService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenArticleLinkIsInvalid() {
		
		excE.expect(ResourceNotFoundException.class);
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
		
		excE.expect(ResourceNotFoundException.class);
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
	
	@Test
	public void shouldSuccessfullyAddNewArticle() throws BusinessException {
		Long editorID = 1L;
		final String TITLE = "Testing title";
		final String LINK = "testing-title";
		DisplaySingleArticleTO newArticle = new DisplaySingleArticleTO.Builder().title(TITLE).link(LINK).build();
		List<ArticleLinkTO> articlesBefore = articleService.getAllArticles();
		List<HistoryTO> historyBefore = historyService.getAllLogEntriesByEditor(editorID);
		
		DisplaySingleArticleTO savedArticle = articleService.saveArticle(newArticle, editorID);
		List<ArticleLinkTO> articlesAfter = articleService.getAllArticles();
		List<String> titlesAfter = articlesAfter.stream().map(a -> a.getTitle()).collect(Collectors.toList());
		List<HistoryTO> historyAfter = historyService.getAllLogEntriesByEditor(editorID);
		
		assertThat(articlesAfter.size() - articlesBefore.size()).isEqualTo(1);
		assertThat(savedArticle.getLink()).isEqualTo(LINK);
		assertThat(titlesAfter).contains(TITLE);
		assertThat(historyAfter.size() - historyBefore.size()).isEqualTo(1);
		assertThat(historyAfter.get(0).getAction()).isEqualTo(LogMessages.ARTICLE_ADDED + TITLE);
	}
	
	
	@Test
	public void shouldThrowArticleExceptionWhenAddingAlreadyExistingLink() throws BusinessException {
		Long editorID = 1L;
		final String TITLE = "New Title";
		final String LINK = "new-title";
		DisplaySingleArticleTO newArticle = new DisplaySingleArticleTO.Builder().title(TITLE).link(LINK).build();
		
		articleService.saveArticle(newArticle, editorID);
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.LINK_EXISTS);
		articleService.saveArticle(newArticle, editorID);
	}
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenEditedArticleLinkIsInvalid() {
		
		excE.expect(ResourceNotFoundException.class);
		articleService.getArticleByLinkToEdit(TestConstants.INVALID_LINK);
	}
	
	@Test
	public void shouldReturnArticleToEditWhenLinkExists() {
		EditArticleTO edit = articleService.getArticleByLinkToEdit(TestConstants.CHILD_2_1_LINK);
		
		assertThat(edit.getTitle()).isEqualTo(TestConstants.CHILD_2_1_TITLE);
		assertThat(edit.getOldLink()).isEqualTo(edit.getLink());
	}
	
	@Test
	public void shouldSuccessfullyEditTitle() throws BusinessException {
		EditArticleTO tbe = new EditArticleTO.Builder()
				.title(TestConstants.EDITED_TITLE)
				.link(TestConstants.EDITED_LINK)
				.oldLink(TestConstants.CHILD_1_LINK)
				.build();
		
		EditArticleTO edited = articleService.editTitle(tbe, 1L);
		
		assertThat(edited.getLink()).isEqualTo(TestConstants.EDITED_LINK);
		assertThat(edited.getOldLink()).isEqualTo(edited.getLink());
	}
	
	@Test
	public void shouldThrowLinkExistsExceptionWhenAnotherArticleHasThatLinkAleardy() throws BusinessException {
		EditArticleTO tbe = new EditArticleTO.Builder()
				.title(TestConstants.EDITED_TITLE)
				.link(TestConstants.CHILD_1_LINK)
				.oldLink(TestConstants.CHILD_2_1_LINK)
				.build();
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.LINK_EXISTS);
		articleService.editTitle(tbe, 1L);
	}
	
	@Test
	public void shouldReturnAllUnpinnedArticlesSortedByTitleAsc() {
		List<ArticleLinkTO> links = articleService.getAllUnpinnedArticles();
		List<String> titles = links.stream().map(l -> l.getLink()).collect(Collectors.toList());
		
		assertThat(titles).containsExactly(TestConstants.COOKIES_POLICY_LINK, TestConstants.PRIVACY_POLICY_LINK);
	}
	
	@Test
	public void shouldSuccessfullyPinToMainMenu() throws BusinessException {
		String link = TestConstants.PRIVACY_POLICY_LINK;
		List<ArticleLinkTO> menuBefore = articleService.getMainMenu();
		
		ArticleLinkTO articleLink = articleService.managePinningToMainMenu(link, 1L, true);
		DisplaySingleArticleTO changed = articleService.getArticleByLink(link);
		List<ArticleLinkTO> menuAfter = articleService.getMainMenu();
		
		assertThat(menuAfter.size() - menuBefore.size()).isEqualTo(1);
		assertThat(changed.isMainMenu()).isTrue();
		assertThat(menuAfter).contains(articleLink);
	}
	
	@Test
	public void shouldThrowExceptionWhenUnpinningAndUserIDInvalid() throws BusinessException {
		String link = TestConstants.PRIVACY_POLICY_LINK;
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		articleService.managePinningToMainMenu(link, 100L, true);
	}
	
	@Test
	public void shouldNotPinAnotherWhenAlradyPinned() throws BusinessException {
		String link = TestConstants.MAIN_PAGE_LINK;
		List<ArticleLinkTO> menuBefore = articleService.getMainMenu();
		
		ArticleLinkTO articleLink = articleService.managePinningToMainMenu(link, 1L, true);
		DisplaySingleArticleTO changed = articleService.getArticleByLink(link);
		List<ArticleLinkTO> menuAfter = articleService.getMainMenu();
		
		assertThat(menuAfter.size() - menuBefore.size()).isEqualTo(0);
		assertThat(changed.isMainMenu()).isTrue();
		assertThat(menuAfter).contains(articleLink);
	}
	
	@Test
	public void shouldSuccessfullyUnPinFromMainMenu() throws BusinessException {
		String link = TestConstants.MAIN_PAGE_LINK;
		List<ArticleLinkTO> menuBefore = articleService.getMainMenu();
		
		ArticleLinkTO articleLink = articleService.managePinningToMainMenu(link, 1L, false);
		DisplaySingleArticleTO changed = articleService.getArticleByLink(link);
		List<ArticleLinkTO> menuAfter = articleService.getMainMenu();
		
		assertThat(menuBefore.size() - menuAfter.size()).isEqualTo(1);
		assertThat(changed.isMainMenu()).isFalse();
		assertThat(menuAfter).doesNotContain(articleLink);
	}
	
	@Test
	public void shouldThrowExceptionWhenPinningAndUserIDInvalid() throws BusinessException {
		String link = TestConstants.MAIN_PAGE_LINK;
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		articleService.managePinningToMainMenu(link, 101L, false);
	}
	
	@Test
	public void shouldNotUnpinnWhenAlreadyUnpinned() throws BusinessException {
		String link = TestConstants.PRIVACY_POLICY_LINK;
		List<ArticleLinkTO> menuBefore = articleService.getMainMenu();
		
		ArticleLinkTO articleLink = articleService.managePinningToMainMenu(link, 1L, false);
		DisplaySingleArticleTO changed = articleService.getArticleByLink(link);
		List<ArticleLinkTO> menuAfter = articleService.getMainMenu();
		
		assertThat(menuBefore.size() - menuAfter.size()).isEqualTo(0);
		assertThat(changed.isMainMenu()).isFalse();
		assertThat(menuAfter).doesNotContain(articleLink);
	}
	
	@Test
	public void shouldSuccessfullyAddNewContent() throws BusinessException {
		DisplaySingleArticleTO articleBefore = articleService.getArticleByLink(TestConstants.CHILD_1_LINK);
		DisplaySingleArticleTO changed = new DisplaySingleArticleTO.Builder()
				.link(TestConstants.CHILD_1_LINK)
				.content(TestConstants.MAIN_PAGE_CONTENT)
				.build();
		
		DisplaySingleArticleTO articleAfter = articleService.editContent(changed, 2L);
		
		assertThat(articleAfter.getContent()).isEqualTo(TestConstants.MAIN_PAGE_CONTENT);
		assertThat(articleAfter.getContentChangesNumber() - articleBefore.getContentChangesNumber()).isEqualTo(1);
		assertThat(articleAfter.getEditedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(articleAfter.getContent()).isNotEqualTo(articleBefore.getContent());
	}
	
	@Test
	public void shouldThrowExceptionWhenChangingContentForInvalidLink() throws BusinessException {
		DisplaySingleArticleTO changed = new DisplaySingleArticleTO.Builder()
				.link(TestConstants.INVALID_LINK)
				.content(TestConstants.MAIN_PAGE_CONTENT)
				.build();
		
		excE.expect(ResourceNotFoundException.class);
		articleService.editContent(changed, 2L);
	}
	
	@Test
	public void shouldThrowExceptionWhenChangingContentForInvalidEditor() throws BusinessException {
		DisplaySingleArticleTO changed = new DisplaySingleArticleTO.Builder()
				.link(TestConstants.CHILD_1_LINK)
				.content(TestConstants.MAIN_PAGE_CONTENT)
				.build();
		
		excE.expect(EditorException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_EDITOR_ID);
		articleService.editContent(changed, 2008L);
	}
	
	@Test
	public void shouldSuccessfullyPinChild() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.PRIVACY_POLICY_LINK;
		Long editorID = 1L;
		
		DisplaySingleArticleTO result = articleService.managePinningChildren(parentLink, childLink, editorID, true);
		List<String> childrenTitles = result.getChildren().stream().map(c -> c.getTitle()).collect(Collectors.toList());
		
		assertThat(childrenTitles).contains(TestConstants.PRIVACY_POLICY_TITLE);
		assertThat(result.getChildren().get(0).getLink()).isNotEqualTo(childLink);
	}
	
	@Test
	public void shouldSuccessfullyUnpinChild() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.CHILD_1_LINK;
		Long editorID = 1L;
		
		DisplaySingleArticleTO result = articleService.managePinningChildren(parentLink, childLink, editorID, false);
		List<String> childrenTitles = result.getChildren().stream().map(c -> c.getTitle()).collect(Collectors.toList());
		
		assertThat(childrenTitles).doesNotContain(TestConstants.CHILD_1_TITLE);
	}
	
	@Test
	public void shouldThrowExceptionWhenPinningToSelf() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.PARENT_LINK;
		Long editorID = 1L;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNING_TO_SELF);
		articleService.managePinningChildren(parentLink, childLink, editorID, true);
	}
	
	@Test
	public void shouldThrowExceptionWhenArticlePinnedToAnother() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.CHILD_2_1_LINK;
		Long editorID = 1L;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNED_TO_ANOTHER);
		articleService.managePinningChildren(parentLink, childLink, editorID, true);
	}
	
	@Test
	public void shouldThrowExceptionWhenArticlePinnedToMainMenu() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.MAIN_PAGE_LINK;
		Long editorID = 1L;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNED_TO_ANOTHER);
		articleService.managePinningChildren(parentLink, childLink, editorID, true);
	}
	
	@Test
	public void shouldSuccessfullyAddNewAttachment() throws BusinessException {
		AttachmentTO ato = new AttachmentTO.Builder()
				.displayName(TestConstants.ATTACHMENT_3_TITLE)
				.fileName(TestConstants.ATTACHMENT_3_NAME)
				.extension(TestConstants.ATTACHMENT_ODT)
				.size(0L)
				.build();
		
		DisplaySingleArticleTO article = articleService.addAttachment(TestConstants.PARENT_LINK, ato, 2L);
		int last = article.getAttachments().size() - 1;
		
		assertThat(article.getAttachments().size()).isGreaterThan(0);
		assertThat(article.getAttachments().get(last).getAddedBy()).isEqualTo(TestConstants.EDITOR_2);
		assertThat(article.getAttachments().get(last).getFileName()).isEqualTo(TestConstants.ATTACHMENT_3_NAME);
	}
	
	@Test
	public void shouldThrowExceptionWhenAttachmentAlreadyInDB() throws BusinessException {
		AttachmentTO ato = new AttachmentTO.Builder()
				.displayName(TestConstants.ATTACHMENT_3_TITLE)
				.fileName(TestConstants.ATTACHMENT_2_NAME)
				.extension(TestConstants.ATTACHMENT_ODT)
				.size(0L)
				.build();
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_EXISTS);
		articleService.addAttachment(TestConstants.PARENT_LINK, ato, 2L);
	}
	
	@Test
	public void shouldSuccessfullyDeleteAttachment() throws BusinessException {
		Long editorID = 2L;
		String fileName = TestConstants.ATTACHMENT_2_NAME;
		String link = TestConstants.PARENT_LINK;
		DisplaySingleArticleTO before = articleService.getArticleByLink(link);
		
		DisplaySingleArticleTO after = articleService.deleteAttachment(link, fileName, editorID);
		List<String> fileNames = after.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(before.getAttachments().size() - after.getAttachments().size()).isEqualTo(1);
		assertThat(fileNames).doesNotContain(fileName);
	}
	
	@Test
	public void shouldNotDeleteAttachmentWhenItExistButNotInGivenArticle() throws BusinessException {
		Long editorID = 2L;
		String fileName = TestConstants.ATTACHMENT_4_NAME;
		String link = TestConstants.PARENT_LINK;
		DisplaySingleArticleTO before = articleService.getArticleByLink(link);
		
		DisplaySingleArticleTO after = articleService.deleteAttachment(link, fileName, editorID);
		DisplaySingleArticleTO origin = articleService.getArticleByLink(TestConstants.NO_CHILDREN_LINK);
		List<String> fileNames = origin.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(before.getAttachments().size()).isEqualTo(after.getAttachments().size());
		assertThat(fileNames).contains(fileName);
	}
	
	@Test
	public void shouldThrowExceptionWhenAttachmentNotInDB() throws BusinessException {
		Long editorID = 2L;
		String fileName = TestConstants.ATTACHMENT_3_TITLE;
		String link = TestConstants.PARENT_LINK;
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_NOT_EXISTS);
		articleService.deleteAttachment(link, fileName, editorID);
	}
}
 