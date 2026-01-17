package com.arqivame.drive.infrastructure.folder;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.gateway.command.FolderCommandGateway;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.infrastructure.acl.persistence.FolderAclJpaEntity;
import com.arqivame.drive.infrastructure.exception.DuplicateEntityException;
import com.arqivame.drive.infrastructure.exception.EntityNotFoundException;
import com.arqivame.drive.infrastructure.folder.persistence.FolderJpaEntity;
import com.arqivame.drive.infrastructure.folder.persistence.FolderJpaRepository;

@Component
public class DefaultFolderGateway implements FolderCommandGateway, FolderQueryGateway {

    private final FolderJpaRepository folderJpaRepository;

    public DefaultFolderGateway(final FolderJpaRepository folderJpaRepository) {
        this.folderJpaRepository = folderJpaRepository;
    }

    @Override
    public Optional<Folder> findRootByOwner(final UserID owner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findRootByOwner'");
    }

    @Override
    public Optional<Folder> findVisibleById(final FolderID id, final UserID actor) {

        final Specification<FolderJpaEntity> specification = folderByIdSpecification(id.getValue())
                .and(folderAclSpecification(actor.getValue()));

        return folderJpaRepository
                .findOne(specification)
                .map(FolderJpaEntity::toDomain);
    }

    @Override
    public Set<Folder> findByParentFolderId(final FolderID parentFolderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByParentFolderId'");
    }

    @Override
    public Set<Folder> findVisibleByParentFolderId(final FolderID parentFolderId, final UserID actor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findVisibleByParentFolderId'");
    }

    @Override
    public Folder create(final Folder folder) {

        if (folderJpaRepository.existsById(folder.getId().getValue()))
            throw DuplicateEntityException.with(folder);

        return save(folder);
    }

    @Override
    public Folder update(final Folder folder) {
        if (!folderJpaRepository.existsById(folder.getId().getValue()))
            throw EntityNotFoundException.with(folder);

        return save(folder);
    }

    private Folder save(final Folder folder) {
        folderJpaRepository.saveAndFlush(Objects.requireNonNull(FolderJpaEntity.fromDomain(folder)));
        return folder;
    }

    private static Specification<FolderJpaEntity> folderByIdSpecification(final UUID folderId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), folderId));
    }

    private static Specification<FolderJpaEntity> folderAclSpecification(final UUID actorId) {
        return (root, query, criteriaBuilder) -> {

            if (query == null)
                return criteriaBuilder.conjunction();

            final var subquery = query.subquery(UUID.class);
            final var aclRoot = subquery.from(FolderAclJpaEntity.class);

            subquery.select(aclRoot.get("id").get("folderId"))
                    .where(
                            criteriaBuilder.and(
                                    criteriaBuilder.equal(
                                            aclRoot.get("id").get("folderId"),
                                            root.get("id")),
                                    criteriaBuilder.equal(
                                            aclRoot.get("id").get("memberId"),
                                            actorId)));

            return criteriaBuilder.exists(subquery);

        };
    }

}
