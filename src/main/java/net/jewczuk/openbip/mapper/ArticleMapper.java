package net.jewczuk.openbip.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@Component
public class ArticleMapper {
	
	public DisplaySingleArticleTO mapToDisplaySingleArticle(ArticleEntity article) {
		List<ContentHistoryEntity> contentHistory = article.getContentHistory().stream().collect(Collectors.toList());
		int lastElement = contentHistory.size() - 1;
		
		DisplaySingleArticleTO saTO = new DisplaySingleArticleTO.Builder()
				.title(article.getTitle())
				.link(article.getLink())
				.content(contentHistory.get(lastElement).getContent())
				.createdAt(contentHistory.get(0).getCreatedAt())
				.editedAt(contentHistory.get(lastElement).getCreatedAt())
				.createdBy(contentHistory.get(0).getEditor().getFullName())
				.createdBy(contentHistory.get(lastElement).getEditor().getFullName())
				.build();
		
		return saTO;
	}

}
