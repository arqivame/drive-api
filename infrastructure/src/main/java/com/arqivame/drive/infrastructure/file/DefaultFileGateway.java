package com.arqivame.drive.infrastructure.file;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.file.FileName;
import com.arqivame.drive.domain.file.gateway.command.FileCommandGateway;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.infrastructure.acl.persistence.FileAclJpaEntity;
import com.arqivame.drive.infrastructure.exception.DuplicateEntityException;
import com.arqivame.drive.infrastructure.exception.EntityNotFoundException;
import com.arqivame.drive.infrastructure.file.persistence.FileJpaEntity;
import com.arqivame.drive.infrastructure.file.persistence.FileJpaRepository;
import com.arqivame.drive.infrastructure.file.persistence.FileSharingJpaEntity;
import com.arqivame.drive.infrastructure.file.persistence.FileSharingJpaRepository;

import jakarta.persistence.criteria.Root;

@Component
public class DefaultFileGateway implements FileQueryGateway, FileCommandGateway {

    private final FileJpaRepository fileJpaRepository;
    private final FileSharingJpaRepository fileSharingRepository;

    public DefaultFileGateway(
            final FileJpaRepository fileJpaRepository,
            final FileSharingJpaRepository fileSharingRepository) {
        this.fileJpaRepository = requireNonNull(fileJpaRepository);
        this.fileSharingRepository = requireNonNull(fileSharingRepository);
    }

    @Override
    public File create(final File file) {

        if (fileJpaRepository.existsById(Objects.requireNonNull(file.getId().getValue())))
            throw DuplicateEntityException.with(file);

        return save(file);
    }

    @Override
    public File update(final File file) {

        if (fileJpaRepository.existsById(Objects.requireNonNull(file.getId().getValue())))
            return save(file);

        throw EntityNotFoundException.with(file);
    }

    @Override
    public Optional<File> findById(final FileID id) {
        return fileJpaRepository
                .findById(id.getValue())
                .map(this::mapToDomain);
    }

    @Override
    public Optional<File> findVisibleById(final FileID id, final UserID actor) {
        return fileJpaRepository
                .findOne(fileByIdSpecification(id.getValue()).and(fileAclSpecification(actor.getValue())))
                .map(this::mapToDomain);
    }

    @Override
    public Boolean existsFileWithNameInFolder(final FileName name, final FolderID folderId) {
        return fileJpaRepository.existsByNameAndFolderId(name.value(), folderId.getValue());
    }

    @Override
    public List<File> findAllInFolder(final FolderID folderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listFilesInFolder'");
    }

    @Override
    public Long totalBytesUsedBy(final UserID owner) {
        return fileJpaRepository.totalContentSizeByOwner(owner.getValue());
    }

    private File save(final File file) {
        fileJpaRepository.saveAndFlush(Objects.requireNonNull(FileJpaEntity.fromDomain(file)));
        return file;
    }

    private File mapToDomain(final FileJpaEntity entity) {
        final List<FileSharingJpaEntity> sharings = this.fileSharingRepository.findAllByFileId(entity.getId());
        return entity.toDomain(
                sharings
                        .stream()
                        .map(FileSharingJpaEntity::toDomain)
                        .collect(Collectors.toSet()));
    }

    private static Specification<FileJpaEntity> fileByIdSpecification(final UUID fileId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), fileId));
        };
    }

    private static Specification<FileJpaEntity> fileAclSpecification(final UUID actorId) {
        return (root, query, criteriaBuilder) -> {

            if (query == null)
                return criteriaBuilder.conjunction();

            final Root<FileAclJpaEntity> aclRoot = query.from(FileAclJpaEntity.class);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("id"), aclRoot.get("id").get("fileId")),
                    criteriaBuilder.equal(aclRoot.get("id").get("userId"), actorId));

        };
    }

}
