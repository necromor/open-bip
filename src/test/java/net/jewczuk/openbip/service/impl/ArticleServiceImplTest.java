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

import net.jewczuk.openbip.service.ArticleService;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ArticleServiceImplTest {
	
	private static final String EDITOR_1 = "Michał Niewiadomy";
	private static final String EDITOR_2 = "Ewelina Test";
	private static final String EDITOR_3 = "Aaron Rodgers";
	private static final String CHILD_1_TITLE = "Dziecko nr 1";
	private static final String CHILD_1_LINK = "dziecko-nr-1";
	private static final String CHILD_1_CONTENT = "Artykuł dziecko 1 v2";
	private static final String CHILD_2_TITLE = "Dziecko nr 2";
	private static final String CHILD_2_LINK = "dziecko-nr-2";

	@Autowired
	private ArticleService articleService;
	
	@Rule
    public ExpectedException excE = ExpectedException.none();
	
	@Test
	public void shouldThrowEmptyResultDataAccessExceptionWhenGivenLinkIsInvalid() {
		
		excE.expect(EmptyResultDataAccessException.class);
		articleService.getArticleByLink("nie-ma-takiego-artykulu");
	}
	
	@Test
	public void shouldReturnArticleWithoutAttachmentsNoChildrenAndNoHistory() {
		DisplaySingleArticleTO article = articleService.getArticleByLink(CHILD_2_LINK);
		
		assertThat(article.getTitle()).isEqualTo(CHILD_2_TITLE);
		assertThat(article.getAttachments()).isEmpty();
		assertThat(article.getChildren()).isEmpty();
		assertThat(article.getCreatedBy()).isEqualTo(EDITOR_3);
		assertThat(article.getEditedBy()).isEqualTo(EDITOR_3);
		assertThat(article.getCreatedAt()).isEqualTo(article.getEditedAt());
	}
	
	@Test
	public void shouldReturnArticleWithContentHistory() {
		DisplaySingleArticleTO article = articleService.getArticleByLink(CHILD_1_LINK);
		
		assertThat(article.getTitle()).isEqualTo(CHILD_1_TITLE);
		assertThat(article.getContent()).isEqualTo(CHILD_1_CONTENT);
		assertThat(article.getCreatedBy()).isEqualTo(EDITOR_1);
		assertThat(article.getEditedBy()).isEqualTo(EDITOR_1);
		assertThat(article.getCreatedAt()).isNotEqualTo(article.getEditedAt());
	}
	
	@Test
	public void shouldReturArticleWithChildrenAndAttachments() {
		List<ArticleLinkTO> children = new ArrayList<>();
		children.add(new ArticleLinkTO(CHILD_2_LINK, CHILD_2_TITLE));
		children.add(new ArticleLinkTO(CHILD_1_LINK, CHILD_1_TITLE));
		List<String> expectedAttachments = Arrays.asList("zal_nr_2.pdf", "zal_nr_1.pdf");
		
		DisplaySingleArticleTO article = articleService.getArticleByLink("artykul-rodzic");
		List<String> actualAttachments = article.getAttachments().stream().map(a -> a.getFileName()).collect(Collectors.toList());
		
		assertThat(article.getContent()).isEqualTo("");
		assertThat(article.getChildren()).containsExactlyElementsOf(children);
		assertThat(actualAttachments).containsExactlyElementsOf(expectedAttachments);
	}
	
	@Test
	public void shouldReturnArticleWithContentHistoryFromDifferentEditors() {
		DisplaySingleArticleTO article = articleService.getArticleByLink("artykul-bez-dzieci");
		
		assertThat(article.getTitle()).isEqualTo("Artykuł bez dzieci");
		assertThat(article.getContent()).isEqualTo("Artykuł bez dzieci v3");
		assertThat(article.getCreatedBy()).isEqualTo(EDITOR_2);
		assertThat(article.getEditedBy()).isEqualTo(EDITOR_1);
		assertThat(article.getAttachments().size()).isEqualTo(3);
		assertThat(article.getAttachments()).allMatch(a -> a.getExtension().equals("odt"));
	}
}
