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
	
	private static final String MAIN_PAGE_TITLE = "Witaj na stronie Open Bip";
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenGivenLinkIsInvalid() {
		
		excE.expect(EmptyResultDataAccessException.class);
		articleRepository.getArticleByLink("nie-ma-takiego-artykulu");
	}
	
	@Test
	public void shouldReturnMainPageArticle() {
		ArticleEntity article = articleRepository.getArticleByLink(ApplicationProperties.MAIN_PAGE_LINK);
		
		assertThat(article.getTitle()).isEqualTo(MAIN_PAGE_TITLE);
		assertThat(article.getAttachments()).isEmpty();
		assertThat(article.getChildren()).isEmpty();
	}
	
	@Test
	public void shouldReturnArticleWithChildrenAndAttachments() {
		ArticleEntity article = articleRepository.getArticleByLink("artykul-rodzic");
		List<String> childrenTitles = article.getChildren().stream().map(a -> a.getTitle()).collect(Collectors.toList());
		List<String> attachmentsFiles = article.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(article.getChildren().size()).isEqualTo(2);
		assertThat(childrenTitles).containsExactlyInAnyOrder("Dziecko nr 1", "Dziecko nr 2");
		assertThat(attachmentsFiles).containsExactlyInAnyOrder("zal_nr_1.pdf", "zal_nr_2.pdf");
	}
	
	@Test
	public void shouldReturnContentChangesWithDiferentAuthors() {
		ArticleEntity article = articleRepository.getArticleByLink("artykul-bez-dzieci");
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
		ArticleEntity article = articleRepository.getArticleByLink("dziecko-nr-1");
		EditorEntity editor1 = editorRepository.getOne(1L);
		EditorEntity editor2 = editorRepository.getOne(2L);
		EditorEntity editor3 = editorRepository.getOne(3L);
		
		assertThat(article.getContentHistory().size()).isEqualTo(2);
		assertThat(article.getContentHistory()).allMatch(ch -> ch.getEditor().equals(editor1));
		assertThat(article.getContentHistory()).noneMatch(ch -> ch.getEditor().equals(editor2));
		assertThat(article.getContentHistory()).noneMatch(ch -> ch.getEditor().equals(editor3));
	}

}
