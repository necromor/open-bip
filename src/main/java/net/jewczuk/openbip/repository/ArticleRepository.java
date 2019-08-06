package net.jewczuk.openbip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.ArticleEntity;

public interface ArticleRepository 
	extends JpaRepository<ArticleEntity, Long>, CustomArticleRepository {
	
	List<ArticleEntity> findAllByOrderByTitleAsc();

}
