package net.jewczuk.openbip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.jewczuk.openbip.entity.SandboxEntity;

public interface SandboxRepository extends JpaRepository<SandboxEntity, Long> {

}
