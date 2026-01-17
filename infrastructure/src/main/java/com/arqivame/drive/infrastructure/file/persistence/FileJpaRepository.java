package com.arqivame.drive.infrastructure.file.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FileJpaRepository extends JpaRepository<FileJpaEntity, UUID>, JpaSpecificationExecutor<FileJpaEntity> {

    Boolean existsByNameAndFolderId(String name, UUID folderId);

    @Query("""
                select
                        coalesce(sum(f.contentSize), 0)
                from File f
                where f.ownerId = :ownerId
            """)
    Long totalContentSizeByOwner(UUID ownerId);

}
