package net.jewczuk.openbip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.SandboxEntity;

public interface SandboxRepository extends JpaRepository<SandboxEntity, Long> {
	
	List<SandboxEntity> findAllByEditorId(Long editorID);

}
