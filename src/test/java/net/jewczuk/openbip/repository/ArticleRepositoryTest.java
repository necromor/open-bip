package net.jewczuk.openbip.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.constants.ExceptionsMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.BusinessException;
import net.jewczuk.openbip.exceptions.ResourceNotFoundException;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ArticleRepositoryTest {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenGivenLinkIsInvalid() {
		
		excE.expect(ResourceNotFoundException.class);
		articleRepository.getArticleByLink(TestConstants.INVALID_LINK);
	}
	
	@Test
	public void shouldReturnMainPageArticle() {
		ArticleEntity article = articleRepository.getArticleByLink(ApplicationProperties.MAIN_PAGE_LINK);
		
		assertThat(article.getTitle()).isEqualTo(TestConstants.MAIN_PAGE_TITLE);
		assertThat(article.getAttachments()).isEmpty();
		assertThat(article.getChildren()).isEmpty();
	}
	
	@Test
	public void shouldReturnArticleWithChildrenAndAttachments() {
		ArticleEntity article = articleRepository.getArticleByLink(TestConstants.PARENT_LINK);
		List<String> childrenTitles = article.getChildren().stream().map(a -> a.getTitle()).collect(Collectors.toList());
		List<String> attachmentsFiles = article.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(article.getChildren().size()).isEqualTo(2);
		assertThat(childrenTitles).containsExactlyInAnyOrder(TestConstants.CHILD_1_TITLE, TestConstants.CHILD_2_TITLE);
		assertThat(attachmentsFiles).containsExactlyInAnyOrder(TestConstants.ATTACHMENT_1_NAME, TestConstants.ATTACHMENT_2_NAME);
	}
	
	@Test
	public void shouldReturnContentChangesWithDiferentAuthors() {
		ArticleEntity article = articleRepository.getArticleByLink(TestConstants.NO_CHILDREN_LINK);
		EditorEntity editor1 = editorRepository.getOne(1L);
		EditorEntity editor2 = editorRepository.getOne(2L);
		EditorEntity editor3 = editorRepository.getOne(3L);
		
		assertThat(article.getContentHistory().size()).isEqualTo(3);
		assertThat(article.getContentHistory()).anySatisfy(ch -> ch.getEditor().equals(editor1));
		assertThat(article.getContentHistory()).anySatisfy(ch -> ch.getEditor().equals(editor2));
		assertThat(article.getContentHistory()).anySatisfy(ch -> ch.getEditor().equals(editor3));
	}
	
	@Test
	public void shouldReturnContentChangesWithOneAuthor() {
		ArticleEntity article = articleRepository.getArticleByLink(TestConstants.CHILD_1_LINK);
		EditorEntity editor1 = editorRepository.getOne(1L);
		EditorEntity editor2 = editorRepository.getOne(2L);
		EditorEntity editor3 = editorRepository.getOne(3L);
		
		assertThat(article.getContentHistory().size()).isEqualTo(2);
		assertThat(article.getContentHistory()).allMatch(ch -> ch.getEditor().equals(editor1));
		assertThat(article.getContentHistory()).noneMatch(ch -> ch.getEditor().equals(editor2));
		assertThat(article.getContentHistory()).noneMatch(ch -> ch.getEditor().equals(editor3));
	}

	
	@Test
	public void shouldReturnOnlyMainMenuArticles() {
		List<ArticleEntity> mainMenu = articleRepository.getMainMenu();
		List<String> links = mainMenu.stream().map(a -> a.getLink()).collect(Collectors.toList());
		
		assertThat(mainMenu.size()).isEqualTo(3);
		assertThat(links).containsExactly(TestConstants.MAIN_PAGE_LINK, TestConstants.NO_CHILDREN_LINK, TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldThrowExceptionWhenArticleHasNoParent() {
		ArticleEntity article = articleRepository.getParentArticle(TestConstants.MAIN_PAGE_LINK);
		
		assertThat(article).isNull();
	}
	
	@Test
	public void shouldReturnParent() {
		ArticleEntity article = articleRepository.getParentArticle(TestConstants.CHILD_1_LINK);
		
		assertThat(article.getLink()).isEqualTo(TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldReturnEmptyBreadCrumbsWhenArticlehasNoParent() {
		List<ArticleEntity> breadCrumbs = articleRepository.getBreadcrumbs(TestConstants.MAIN_PAGE_LINK);
		
		assertThat(breadCrumbs).isEmpty();
	}
	
	@Test
	public void shouldReturnOneElementWhenArticleIsInLevel2() {
		List<ArticleEntity> breadCrumbs = articleRepository.getBreadcrumbs(TestConstants.CHILD_1_LINK);
		
		assertThat(breadCrumbs.size()).isEqualTo(1);
		assertThat(breadCrumbs.get(0).getTitle()).isEqualTo(TestConstants.PARENT_TITLE);
	}
	
	@Test
	public void shouldReturnTwoElementsInCorrectOrderWhenArticleIsInLevel3() {
		List<ArticleEntity> breadCrumbs = articleRepository.getBreadcrumbs(TestConstants.CHILD_2_1_LINK);
		List<String> links = breadCrumbs.stream().map(a -> a.getLink()).collect(Collectors.toList());
		
		assertThat(links).contains(TestConstants.CHILD_2_LINK, TestConstants.PARENT_LINK);
	}
	
	@Test
	public void shouldReturnAllArticlesOrderdByTitleAsc() {
		List<ArticleEntity> articles = articleRepository.findAllByOrderByTitleAsc();
		List<String> titles = articles.stream().map(a -> a.getTitle()).collect(Collectors.toList());
		
		assertThat(articles.size()).isEqualTo(8);
		assertThat(titles.get(0)).isEqualTo(TestConstants.PARENT_TITLE);
		assertThat(titles.get(7)).isEqualTo(TestConstants.PRIVACY_POLICY_TITLE);
	}
	
	@Test
	public void shouldReturnAllUnpinnedArticles() {
		List<ArticleEntity> articles = articleRepository.getUnpinnedArticles();
		List<String> titles = articles.stream().map(a -> a.getTitle()).collect(Collectors.toList());
		
		assertThat(titles).containsExactly(TestConstants.COOKIES_POLICY_TITLE, TestConstants.PRIVACY_POLICY_TITLE);
	}
	
	@Test
	public void shouldSuccessfullyPinnChild() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.COOKIES_POLICY_LINK;
		ArticleEntity parent = articleRepository.getArticleByLink(parentLink);
		ArticleEntity child = articleRepository.getArticleByLink(parentLink);
		
		List<ArticleEntity> articles = articleRepository.managePinningChild(parentLink, childLink);
		
		assertThat(parent.getChildren()).doesNotContain(child);
		assertThat(articles.get(0).getChildren()).contains(articles.get(1));
		assertThat(articles.get(1).getDisplayPosition()).isEqualTo(articles.get(0).getChildren().size());
	}
	
	@Test
	public void shouldSuccessfullyUnpinnChild() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.CHILD_1_LINK;
		
		List<ArticleEntity> articles = articleRepository.managePinningChild(parentLink, childLink);
		
		assertThat(articles.get(0).getChildren()).doesNotContain(articles.get(1));
		assertThat(articles.get(1).getDisplayPosition()).isEqualTo(0);
	}	
	
	@Test
	public void shouldThrowExceptionWhenPinningToSelf() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.PARENT_LINK;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNING_TO_SELF);
		articleRepository.managePinningChild(parentLink, childLink);
	}
	
	@Test
	public void shouldThrowExceptionWhenPinningArticleFromMainMenu() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.NO_CHILDREN_LINK;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNED_TO_ANOTHER);
		articleRepository.managePinningChild(parentLink, childLink);
	}
	
	@Test
	public void shouldThrowExceptionWhenPinningArticleThatHasAnotherParent() throws BusinessException {
		String parentLink = TestConstants.PARENT_LINK;
		String childLink = TestConstants.CHILD_2_1_LINK;
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.PINNED_TO_ANOTHER);
		articleRepository.managePinningChild(parentLink, childLink);
	}
	
	@Test
	public void shouldSuccessfullyCreateNewContent() throws BusinessException {
		DisplaySingleArticleTO changed = new DisplaySingleArticleTO.Builder()
				.link(TestConstants.CHILD_2_LINK)
				.content(TestConstants.MAIN_PAGE_CONTENT)
				.build();
		EditorEntity editor = editorRepository.getEditorById(2L);
		
		ArticleEntity articleAfter = articleRepository.addContent(changed, editor);
		List<String> contents = articleAfter.getContentHistory().stream().map(c -> c.getContent()).collect(Collectors.toList());
		
		assertThat(contents).contains(TestConstants.MAIN_PAGE_CONTENT);
		assertThat(contents).doesNotHaveDuplicates();
	}
	
	@Test
	public void shouldThrowExceptionWhenAddingContentToInvalidLink() throws BusinessException {
		DisplaySingleArticleTO changed = new DisplaySingleArticleTO.Builder()
				.link(TestConstants.INVALID_LINK)
				.content(TestConstants.MAIN_PAGE_CONTENT)
				.build();
		EditorEntity editor = editorRepository.getEditorById(2L);	
		
		excE.expect(ResourceNotFoundException.class);
		articleRepository.addContent(changed, editor);
	}
	
	@Test
	public void shouldPinnToMainMenu() {
		ArticleEntity article = articleRepository.managePinningToMainMenu(TestConstants.COOKIES_POLICY_LINK, true);
		
		assertThat(article.isMainMenu()).isTrue();
		assertThat(article.getDisplayPosition()).isEqualTo(articleRepository.getMainMenu().size());
	}
	
	@Test
	public void shouldUnpinnFromMainMenu() {
		ArticleEntity article = articleRepository.managePinningToMainMenu(TestConstants.NO_CHILDREN_LINK, false);
		
		assertThat(article.isMainMenu()).isFalse();
		assertThat(article.getDisplayPosition()).isEqualTo(0);
	}
	
	@Test
	public void shouldPinnToMainMenuEvenIfAlreadyPinned() {
		ArticleEntity article = articleRepository.managePinningToMainMenu(TestConstants.NO_CHILDREN_LINK, true);
		
		assertThat(article.isMainMenu()).isTrue();
	}
	
	@Test
	public void shouldUnpinnFromMainMenuEvenIfAlreadyUnpinned() {
		ArticleEntity article = articleRepository.managePinningToMainMenu(TestConstants.COOKIES_POLICY_LINK, false);
		
		assertThat(article.isMainMenu()).isFalse();
		assertThat(article.getDisplayPosition()).isEqualTo(0);
	}
	
}
