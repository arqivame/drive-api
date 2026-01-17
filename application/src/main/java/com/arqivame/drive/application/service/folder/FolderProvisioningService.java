package com.arqivame.drive.application.service.folder;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.stream.Collectors;

import com.arqivame.drive.domain.acl.Acl;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.exception.NotFoundException;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.FolderName;
import com.arqivame.drive.domain.folder.gateway.command.FolderCommandGateway;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;
import com.arqivame.drive.domain.folder.service.FolderCreationService;
import com.arqivame.drive.domain.user.UserID;

public class FolderProvisioningService {

    private final EventDispatcher eventDispatcher;
    private final FolderQueryGateway folderQueryGateway;
    private final FolderCommandGateway folderCommandGateway;
    private final AclGateway aclGateway;

    public FolderProvisioningService(
            final EventDispatcher eventDispatcher,
            final FolderQueryGateway folderQueryGateway,
            final FolderCommandGateway folderCommandGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = requireNonNull(eventDispatcher);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.folderCommandGateway = requireNonNull(folderCommandGateway);
        this.aclGateway = requireNonNull(aclGateway);
    }

    public Folder rootFolder(final UserID owner) {

        final FolderCreationService.Result folderProvisioningResult = FolderCreationService.createRootFolder(owner);

        this.eventDispatcher.notify(this.folderCommandGateway.create(folderProvisioningResult.folder()));
        this.eventDispatcher.notify(this.aclGateway.create(folderProvisioningResult.acl()));

        return folderProvisioningResult.folder();

    }

    public Folder folder(
            final FolderID parentFolderId,
            final UserID creatorId,
            final FolderName name) {

        final Folder parentFolder = this.folderQueryGateway
                .findVisibleById(parentFolderId, creatorId)
                .orElseThrow(() -> NotFoundException.create(
                        Folder.class,
                        parentFolderId));

        final Set<FolderName> siblingFolderNames = this.folderQueryGateway
                .findByParentFolderId(parentFolderId)
                .stream()
                .map(Folder::getName)
                .collect(Collectors.toSet());

        final Acl parentFolderAcl = this.aclGateway
                .findByResource(Resource.folder(parentFolder))
                .orElseThrow(() -> NotFoundException.create(
                        Folder.class,
                        parentFolderId));

        final FolderCreationService.Result folderCreationResult = FolderCreationService.createFolder(
                creatorId,
                name,
                parentFolderAcl,
                parentFolder,
                siblingFolderNames);

        this.eventDispatcher.notify(this.folderCommandGateway.create(folderCreationResult.folder()));
        this.eventDispatcher.notify(this.aclGateway.create(folderCreationResult.acl()));

        return folderCreationResult.folder();
    }

}