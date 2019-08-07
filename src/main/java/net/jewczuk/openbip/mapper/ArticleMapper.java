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
		List<ContentHistoryEntity> contentHistory = article.getContentHistory().stream()
				.sorted(Comparator.comparing(ContentHistoryEntity::getCreatedAt))
				.collect(Collectors.toList());
		int lastElement = contentHistory.size() - 1;
		
		DisplaySingleArticleTO saTO = new DisplaySingleArticleTO.Builder()
				.title(article.getTitle())
				.link(article.getLink())
				.content(lastElement == -1 ? null : contentHistory.get(lastElement).getContent())
				.createdAt(lastElement == -1 ? null : contentHistory.get(0).getCreatedAt())
				.editedAt(lastElement == -1 ? null : contentHistory.get(lastElement).getCreatedAt())
				.createdBy(lastElement == -1 ? null : contentHistory.get(0).getEditor().getFullName())
				.editedBy(lastElement == -1 ? null : contentHistory.get(lastElement).getEditor().getFullName())
				.contentChangesNumber(lastElement)
				.mainMenu(article.isMainMenu())
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
	
	public ArticleEntity map2NewAE(DisplaySingleArticleTO aTO) {
		ArticleEntity entity = new ArticleEntity();
		entity.setTitle(aTO.getTitle());
		entity.setLink(aTO.getLink());
		
		return entity;
	}

}
