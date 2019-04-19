package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.AttachmentEntity;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

}
