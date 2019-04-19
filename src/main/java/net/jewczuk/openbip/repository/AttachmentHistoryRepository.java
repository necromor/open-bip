package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.AttachmentHistoryEntity;

public interface AttachmentHistoryRepository extends JpaRepository<AttachmentHistoryEntity, Long> {

}
