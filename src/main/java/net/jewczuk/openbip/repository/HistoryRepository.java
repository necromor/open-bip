package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.HistoryEntity;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

}
