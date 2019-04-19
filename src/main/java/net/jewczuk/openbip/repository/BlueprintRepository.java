package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.BlueprintEntity;

public interface BlueprintRepository extends JpaRepository<BlueprintEntity, Long> {

}
