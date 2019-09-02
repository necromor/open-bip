package net.jewczuk.openbip.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import net.jewczuk.openbip.constants.LogMessages;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.exceptions.ArticleException;
import net.jewczuk.openbip.exceptions.AttachmentException;
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
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
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
	
	@Test
	public void shouldSuccessfullyAddNewAttachment() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(3L);
		AttachmentEntity attachment = new AttachmentEntity();
		attachment.setDisplayName(TestConstants.ATTACHMENT_3_TITLE);
		attachment.setFileName(TestConstants.ATTACHMENT_3_NAME);
		attachment.setSize(32L);
		attachment.setExtension(TestConstants.ATTACHMENT_ODT);
		
		ArticleEntity article = articleRepository.addAttachment(TestConstants.PARENT_LINK, attachment, editor);
		List<AttachmentEntity> list = article.getAttachments().stream()
				.sorted(Comparator.comparing(AttachmentEntity::getDisplayPosition))
				.collect(Collectors.toList());
		AttachmentEntity lastElement = list.get(list.size() - 1);
		List<String> attHistoryLogs = article.getAttachmentsHistory().stream().map(a -> a.getLog()).collect(Collectors.toList());
		
		assertThat(article.getAttachments().size()).isGreaterThan(0);
		assertThat(lastElement.getDisplayName()).isEqualTo(TestConstants.ATTACHMENT_3_TITLE);
		assertThat(lastElement.getFileName()).isEqualTo(TestConstants.ATTACHMENT_3_NAME);
		assertThat(lastElement.getSize()).isEqualTo(32L);
		assertThat(lastElement.getExtension()).isEqualTo(TestConstants.ATTACHMENT_ODT);
		assertThat(lastElement.getDisplayPosition()).isEqualTo(article.getAttachments().size());
		assertThat(lastElement.getAddedBy().getFullName()).isEqualTo(TestConstants.EDITOR_3);
		assertThat(article.getAttachmentsHistory().size()).isGreaterThan(0);
		assertThat(attHistoryLogs).contains(LogMessages.ATTACHMENT_HISTORY_ADD + TestConstants.ATTACHMENT_3_TITLE);
	}
	
	@Test
	public void shouldThrowExceptionWhenAddingFileThatAlreadyExistInDb() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(3L);
		AttachmentEntity attachment = new AttachmentEntity();
		attachment.setDisplayName(TestConstants.ATTACHMENT_3_TITLE);
		attachment.setFileName(TestConstants.ATTACHMENT_1_NAME);
		attachment.setSize(32L);
		attachment.setExtension(TestConstants.ATTACHMENT_ODT);	
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_EXISTS);
		articleRepository.addAttachment(TestConstants.PARENT_LINK, attachment, editor);
	}
	
	@Test
	public void shouldSuccessfullyDeleteAttachment() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(3L);
		String fileName = TestConstants.ATTACHMENT_1_NAME;
		String link = TestConstants.PARENT_LINK;
		
		ArticleEntity article = articleRepository.deleteAttachment(link, fileName, editor);
		List<String> attNames = article.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		List<String> attHistoryLogs = article.getAttachmentsHistory().stream().map(a -> a.getLog()).collect(Collectors.toList());
		AttachmentEntity attachment = attachmentRepository.findByFileName(fileName);
		
		assertThat(attNames).doesNotContain(fileName);
		assertThat(attNames).containsExactly(TestConstants.ATTACHMENT_2_NAME);
		assertThat(attHistoryLogs).contains(LogMessages.ATTACHMENT_HISTORY_REMOVE + TestConstants.ATTACHMENT_1_TITLE);
		assertThat(attachment).isNull();
	}
	
	@Test
	public void shouldNotDeleteAttachmentWhenAttachmentNotInArticle() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(3L);
		String fileName = TestConstants.ATTACHMENT_4_NAME;
		String link = TestConstants.PARENT_LINK;
		
		articleRepository.deleteAttachment(link, fileName, editor);
		ArticleEntity origin = articleRepository.getArticleByLink(TestConstants.NO_CHILDREN_LINK);
		List<String> attNames = origin.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(attNames).contains(TestConstants.ATTACHMENT_4_NAME);
	}
	
	@Test
	public void shouldThrowExceptionWhenAttachmentDoesNotExist() throws BusinessException {
		EditorEntity editor = editorRepository.getEditorById(3L);
		String fileName = TestConstants.ATTACHMENT_3_NAME;
		String link = TestConstants.PARENT_LINK;
		
		excE.expect(AttachmentException.class);
		excE.expectMessage(ExceptionsMessages.ATTACHMENT_NOT_EXISTS);
		articleRepository.deleteAttachment(link, fileName, editor);
	}
	
	@Test
	public void shouldGetAllArticlesHierarchical() {
		List<ArticleEntity> expected = new ArrayList<>();
		expected.add(articleRepository.getArticleByLink(TestConstants.MAIN_PAGE_LINK));
		expected.add(articleRepository.getArticleByLink(TestConstants.PARENT_LINK));
		expected.add(articleRepository.getArticleByLink(TestConstants.NO_CHILDREN_LINK));
		expected.add(articleRepository.getArticleByLink(TestConstants.COOKIES_POLICY_LINK));
		expected.add(articleRepository.getArticleByLink(TestConstants.PRIVACY_POLICY_LINK));
		expected = expected.stream().sorted(Comparator.comparing(ArticleEntity::getTitle)).collect(Collectors.toList());
		
		List<ArticleEntity> tree = articleRepository.getTree();
		
		assertThat(tree).isEqualTo(expected);
		assertThat(tree.get(0).getChildren().size()).isGreaterThan(1);
	}
	
	@Test
	public void shouldSaveNewChildrenPositions() throws BusinessException {
		String link = TestConstants.PARENT_LINK;
		List<String> newPositions = Arrays.asList(TestConstants.CHILD_1_LINK, TestConstants.CHILD_2_LINK);
		
		ArticleEntity result = articleRepository.saveChildrenPositions(link, newPositions);
		List<String> resChildrenTitles = result.getChildren().stream()
				.sorted(Comparator.comparing(ArticleEntity::getDisplayPosition))
				.map(c -> c.getLink())
				.collect(Collectors.toList());
		
		assertThat(resChildrenTitles).containsExactlyElementsOf(newPositions);
	}
	
	@Test
	public void shouldNotChangeChildrenPositionsWhenNoChangeInOrder() throws BusinessException {
		String link = TestConstants.PARENT_LINK;
		List<String> newPositions = Arrays.asList(TestConstants.CHILD_2_LINK, TestConstants.CHILD_1_LINK);
		
		ArticleEntity result = articleRepository.saveChildrenPositions(link, newPositions);
		List<String> resChildrenTitles = result.getChildren().stream()
				.sorted(Comparator.comparing(ArticleEntity::getDisplayPosition))
				.map(c -> c.getLink())
				.collect(Collectors.toList());
		
		assertThat(resChildrenTitles).containsExactlyElementsOf(newPositions);
	}
	
	@Test
	public void shouldThrowExceptionWhenGivenTooManyChildren() throws BusinessException {
		String link = TestConstants.PARENT_LINK;
		List<String> newPositions = Arrays.asList(TestConstants.CHILD_2_LINK, TestConstants.CHILD_1_LINK, TestConstants.CHILD_2_1_LINK);
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_CHILDREN_SIZE);
		articleRepository.saveChildrenPositions(link, newPositions);
	}
	
	@Test
	public void shouldThrowExceptionWhenInvalidChild() throws BusinessException {
		String link = TestConstants.PARENT_LINK;
		List<String> newPositions = Arrays.asList(TestConstants.CHILD_2_1_LINK, TestConstants.CHILD_1_LINK);
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_CHILDREN_SIZE);
		articleRepository.saveChildrenPositions(link, newPositions);
	}
	
	@Test
	public void shouldSaveNewAttachmentsPositions() throws BusinessException {
		String link = TestConstants.NO_CHILDREN_LINK;
		List<String> newPositions = 
				Arrays.asList(TestConstants.ATTACHMENT_5_NAME, TestConstants.ATTACHMENT_6_NAME, TestConstants.ATTACHMENT_4_NAME);
		
		ArticleEntity result = articleRepository.saveAttachmentsPositions(link, newPositions);
		List<String> resAttachmentsNames = result.getAttachments().stream()
				.sorted(Comparator.comparing(AttachmentEntity::getDisplayPosition))
				.map(a -> a.getFileName())
				.collect(Collectors.toList());
		
		assertThat(resAttachmentsNames).containsExactlyElementsOf(newPositions);
	}
	
	@Test
	public void shouldNotChangeAttachmentsPositionsWhenNoChangeInOrder() throws BusinessException {
		String link = TestConstants.NO_CHILDREN_LINK;
		List<String> newPositions = 
				Arrays.asList(TestConstants.ATTACHMENT_4_NAME, TestConstants.ATTACHMENT_5_NAME, TestConstants.ATTACHMENT_6_NAME);
		
		ArticleEntity result = articleRepository.saveAttachmentsPositions(link, newPositions);
		List<String> resAttachmentsNames = result.getAttachments().stream()
				.sorted(Comparator.comparing(AttachmentEntity::getDisplayPosition))
				.map(a -> a.getFileName())
				.collect(Collectors.toList());
		
		assertThat(resAttachmentsNames).containsExactlyElementsOf(newPositions);
	}
	
	@Test
	public void shouldThrowExceptionWhenGivenTooManyAttachments() throws BusinessException {
		String link = TestConstants.NO_CHILDREN_LINK;
		List<String> newPositions = 
				Arrays.asList(TestConstants.ATTACHMENT_5_NAME, TestConstants.ATTACHMENT_6_NAME);
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_ATTACHMENT_SIZE);
		articleRepository.saveAttachmentsPositions(link, newPositions);
	}
	
	@Test
	public void shouldThrowExceptionWhenInvalidAttachment() throws BusinessException {
		String link = TestConstants.NO_CHILDREN_LINK;
		List<String> newPositions = 
				Arrays.asList(TestConstants.ATTACHMENT_4_NAME, TestConstants.ATTACHMENT_5_NAME, TestConstants.ATTACHMENT_4_NAME);
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_ATTACHMENT_SIZE);
		articleRepository.saveAttachmentsPositions(link, newPositions);
	}
	
	@Test
	public void shouldSaveNewMainMenuPositions() throws BusinessException {
		List<String> newPositions = 
				Arrays.asList(TestConstants.NO_CHILDREN_LINK, TestConstants.MAIN_PAGE_LINK, TestConstants.PARENT_LINK);	
		
		List<ArticleEntity> newMenu = articleRepository.saveMenuPositions(newPositions);
		
		assertThat(newMenu).containsExactlyElementsOf(articleRepository.getMainMenu());
	}
	
	@Test
	public void shouldThrowExceptionWhenArticleNotInMainMenu() throws BusinessException {
		List<String> newPositions = 
				Arrays.asList(TestConstants.CHILD_1_LINK, TestConstants.MAIN_PAGE_LINK, TestConstants.PARENT_LINK);	
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.NOT_IN_MAIN_MENU);		
		articleRepository.saveMenuPositions(newPositions);
	}
	
	@Test
	public void shouldThrowExceptionWhenLinksSizeNotEqualToMainMenu() throws BusinessException {
		List<String> newPositions = 
				Arrays.asList(TestConstants.MAIN_PAGE_LINK, TestConstants.PARENT_LINK);	
		
		excE.expect(ArticleException.class);
		excE.expectMessage(ExceptionsMessages.INVALID_MAIN_MENU_SIZE);		
		articleRepository.saveMenuPositions(newPositions);
	}
}
