package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.EditorEntity;

public interface EditorRepository extends JpaRepository<EditorEntity, Long>, CustomEditorRepository {
	
	EditorEntity findByEmail(String email);

}
