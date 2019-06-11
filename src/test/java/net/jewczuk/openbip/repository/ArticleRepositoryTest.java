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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import net.jewczuk.openbip.TestConstants;
import net.jewczuk.openbip.constants.ApplicationProperties;
import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.EditorEntity;

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
		
		excE.expect(EmptyResultDataAccessException.class);
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
}
