package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.ContentHistoryEntity;

public interface ContentHistoryRepository extends JpaRepository<ContentHistoryEntity, Long> {

}
