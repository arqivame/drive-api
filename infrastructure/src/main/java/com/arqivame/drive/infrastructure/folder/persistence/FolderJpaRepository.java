package com.arqivame.drive.infrastructure.folder.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FolderJpaRepository extends
        JpaRepository<FolderJpaEntity, UUID>,
        JpaSpecificationExecutor<FolderJpaEntity> {

    Optional<FolderJpaEntity> findByOwnerIdAndIsRootTrue(UUID ownerId);

}
