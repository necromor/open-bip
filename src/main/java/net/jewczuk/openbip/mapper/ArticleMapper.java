package net.jewczuk.openbip.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.ArticleEntity;
import net.jewczuk.openbip.entity.AttachmentEntity;
import net.jewczuk.openbip.entity.AttachmentHistoryEntity;
import net.jewczuk.openbip.entity.ContentHistoryEntity;
import net.jewczuk.openbip.to.ArticleLinkTO;
import net.jewczuk.openbip.to.DisplayArticleHistoryTO;
import net.jewczuk.openbip.to.DisplaySingleArticleTO;

@Component
public class ArticleMapper {
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	
	@Autowired
	private AttachmentHistoryMapper attachmentHistoryMapper;
	
	@Autowired
	private ContentHistoryMapper contentHistoryMapper;
	
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
				.editedBy(contentHistory.get(lastElement).getEditor().getFullName())
				.contentChangesNumber(lastElement)
				.children(article.getChildren().stream()
						.sorted(Comparator.comparing(ArticleEntity::getDisplayPosition))
						.map(a -> mapToLink(a))
						.collect(Collectors.toList()))
				.attachments(article.getAttachments().stream()
						.sorted(Comparator.comparing(AttachmentEntity::getDisplayPosition))
						.map(a -> attachmentMapper.mapToTO(a))
						.collect(Collectors.toList()))
				.build();
		
		return saTO;
	}
	
	public DisplayArticleHistoryTO mapToHistory(ArticleEntity article) {		
		return new DisplayArticleHistoryTO.Builder()
				.title(article.getTitle())
				.link(article.getLink())
				.contentHistory(article.getContentHistory().stream()
						.sorted(Comparator.comparing(ContentHistoryEntity::getCreatedAt).reversed())
						.map(ch -> contentHistoryMapper.mapToTO(ch))
						.collect(Collectors.toList()))
				.attachmentsHistory(article.getAttachmentsHistory().stream()
						.sorted(Comparator.comparing(AttachmentHistoryEntity::getCreatedAt))
						.map(a -> attachmentHistoryMapper.mapToTO(a))
						.collect(Collectors.toList()))
				.build();
	}
	
	public ArticleLinkTO mapToLink(ArticleEntity article) {
		return new ArticleLinkTO(article.getLink(), article.getTitle());
	}

}
