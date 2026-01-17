package com.arqivame.drive.infrastructure.acl;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Acl;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.acl.Entry;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.infrastructure.acl.persistence.AclJpaEntity;
import com.arqivame.drive.infrastructure.acl.persistence.AclJpaRepository;
import com.arqivame.drive.infrastructure.acl.persistence.EntryJpaEntity;
import com.arqivame.drive.infrastructure.acl.persistence.EntryJpaRepository;
import com.arqivame.drive.infrastructure.acl.persistence.FileAclJpaEntity;
import com.arqivame.drive.infrastructure.acl.persistence.FileAclJpaRepository;
import com.arqivame.drive.infrastructure.acl.persistence.FolderAclJpaEntity;
import com.arqivame.drive.infrastructure.acl.persistence.FolderAclJpaRepository;
import com.arqivame.drive.infrastructure.exception.DuplicateEntityException;

@Component
public class DefaultAclGateway implements AclGateway {

    private final AclJpaRepository aclJpaRepository;
    private final EntryJpaRepository entryJpaRepository;
    private final FolderAclJpaRepository folderAclJpaRepository;
    private final FileAclJpaRepository fileAccessAclJpaRepository;

    public DefaultAclGateway(
            final AclJpaRepository aclJpaRepository,
            final EntryJpaRepository entryJpaRepository,
            final FolderAclJpaRepository folderAclJpaRepository,
            final FileAclJpaRepository fileAccessAclJpaRepository) {
        this.aclJpaRepository = aclJpaRepository;
        this.entryJpaRepository = entryJpaRepository;
        this.folderAclJpaRepository = folderAclJpaRepository;
        this.fileAccessAclJpaRepository = fileAccessAclJpaRepository;
    }

    @Override
    public Acl create(final Acl acl) {

        if (aclJpaRepository.existsById(acl.getId().getValue()))
            throw DuplicateEntityException.with(acl);

        return save(acl);
    }

    @Override
    public Optional<Acl> findByResource(final Resource<?> resource) {

        return aclJpaRepository
                .findOneByResourceIdAndResourceType(resource.getId().getStringValue(), resource.getType())
                .map(this::mapToDomain);

    }

    private Acl save(final Acl acl) {

        if (acl.getResource().getType() == Resource.Type.FOLDER) {
            saveFolderAccessAcl(acl);
        } else if (acl.getResource().getType() == Resource.Type.FILE) {
            saveFileAccessAcl(acl);
        }

        final AclJpaEntity aclJpa = aclJpaRepository.save(AclJpaEntity.fromDomain(acl));

        final var entriesJpa = Stream.concat(
                acl.getDirectEntries()
                        .stream()
                        .map(entry -> EntryJpaEntity.fromDomain(aclJpa, EntryJpaEntity.Type.DIRECT, entry)),
                acl.getInheritedEntries()
                        .stream()
                        .map(entry -> EntryJpaEntity.fromDomain(aclJpa, EntryJpaEntity.Type.INHERITED, entry)))
                .toList();

        this.entryJpaRepository.deleteAllByAclIdAndIdNotIn(
                aclJpa.getId(),
                entriesJpa.stream().map(EntryJpaEntity::getId).toList());

        this.entryJpaRepository.saveAll(entriesJpa);

        return acl;

    }

    private Acl mapToDomain(final AclJpaEntity aclJpa) {

        final Set<Entry> directEntries = entryJpaRepository
                .findAllByAclIdAndType(aclJpa.getId(), EntryJpaEntity.Type.DIRECT)
                .stream()
                .map(EntryJpaEntity::toDomain)
                .collect(Collectors.toSet());

        final Set<Entry> inheritedEntries = entryJpaRepository
                .findAllByAclIdAndType(aclJpa.getId(), EntryJpaEntity.Type.INHERITED)
                .stream()
                .map(EntryJpaEntity::toDomain)
                .collect(Collectors.toSet());

        return aclJpa.toDomain(directEntries, inheritedEntries);

    }

    private void saveFolderAccessAcl(final Acl acl) {

        final var folderAccessAcls = filterEntries(acl)
                .map(entry -> FolderAclJpaEntity.from(
                        acl.getResource().folder(),
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        final var folderAccessAclIds = folderAccessAcls
                .stream()
                .map(FolderAclJpaEntity::getId)
                .collect(Collectors.toSet());

        this.folderAclJpaRepository.deleteAllByIdFolderIdAndIdNotIn(
                acl.getResource().folder().getId().getValue(),
                folderAccessAclIds);

        this.folderAclJpaRepository.saveAll(folderAccessAcls);

    }

    private void saveFileAccessAcl(final Acl acl) {

        final var fileAccessAcls = filterEntries(acl)
                .map(entry -> FileAclJpaEntity.from(
                        acl.getResource().file(),
                        entry.getKey(),
                        entry.getValue()))
                .toList();

        final var fileAccessAclIds = fileAccessAcls
                .stream()
                .map(FileAclJpaEntity::getId)
                .collect(Collectors.toSet());

        this.fileAccessAclJpaRepository.deleteAllByIdFileIdAndIdNotIn(
                acl.getResource().file().getId().getValue(),
                fileAccessAclIds);

        this.fileAccessAclJpaRepository.saveAll(fileAccessAcls);

    }

    private static Stream<Map.Entry<UserID, AccessPermission>> filterEntries(final Acl acl) {

        return Stream
                .concat(acl.getDirectEntries().stream(), acl.getInheritedEntries().stream())
                .collect(Collectors.groupingBy(Entry::user,
                        Collectors.mapping(
                                entry -> (AccessPermission) entry.permission(),
                                Collectors.minBy(Comparator.comparing(AccessPermission::level)))))
                .entrySet()
                .stream()
                .filter(mapEntry -> mapEntry.getValue().isPresent())
                .map(mapEntry -> Map.entry(mapEntry.getKey(), mapEntry.getValue().get()));

    }

}
