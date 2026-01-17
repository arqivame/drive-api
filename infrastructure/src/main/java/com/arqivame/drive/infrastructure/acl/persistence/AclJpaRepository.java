package com.arqivame.drive.infrastructure.acl.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arqivame.drive.domain.acl.Resource;

public interface AclJpaRepository extends JpaRepository<AclJpaEntity, UUID> {

    Optional<AclJpaEntity> findOneByResourceIdAndResourceType(String resourceId, Resource.Type resourceType);

}
