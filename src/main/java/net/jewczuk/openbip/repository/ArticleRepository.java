package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.ArticleEntity;

public interface ArticleRepository 
	extends JpaRepository<ArticleEntity, Long>, CustomArticleRepository {

}
