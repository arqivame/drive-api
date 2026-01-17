package com.arqivame.drive.domain.folder.service;

import java.util.Set;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Acl;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.exception.InvalidArgumentException;
import com.arqivame.drive.domain.exception.NotAllowedException;
import com.arqivame.drive.domain.exception.ValidationException;
import com.arqivame.drive.domain.exception.DomainException.Error;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderName;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.handler.Notification;

public class FolderCreationService {

    public static FolderCreationService.Result createRootFolder(final UserID owner) {

        final Folder folder = Folder.createRoot(owner);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationService.Result(acl, folder);

    }

    public static FolderCreationService.Result createFolder(
            final UserID creator,
            final FolderName name,
            final Acl parentFolderAcl,
            final Folder parentFolder,
            final Set<FolderName> siblingFolderNames) {

        final AccessPermission parentFolderAccessPermission = parentFolderAcl
                .resolveAccessPermissionFor(creator)
                .orElseThrow(() -> NotAllowedException
                        .with("Member [%s] doesn't have any permissions in folder [%s].".formatted(
                                creator,
                                parentFolder.getId())));

        if (!parentFolderAccessPermission.isAtMost(AccessPermission.WRITE))
            throw NotAllowedException
                    .with("Member [%s] doesn't have write permission in folder [%s].".formatted(
                            creator,
                            parentFolder.getId()));

        final Notification notification = Notification.create();

        if (siblingFolderNames.contains(name))
            notification.append(
                    ValidationError.with("Folder name %s already exists in the parent folder".formatted(name.value())));

        final Folder folder = notification
                .validate(() -> Folder.create(creator, name, parentFolder));

        final Acl newFolderAcl = notification.validate(() -> parentFolderAcl.deriveFor(Resource.folder(folder)));

        if (notification.hasErrors())
            throw ValidationException.with("Could not create Aggregate Folder", notification);

        return new FolderCreationService.Result(newFolderAcl, folder);

    }

    public static FolderCreationService.Result createInboxFolder(
            final UserID owner,
            final Folder rootFolder,
            final Set<FolderName> siblingFolderNames) {

        if (!rootFolder.isRoot())
            throw InvalidArgumentException.with(Error.with("The specified folder is not a root folder"));

        Integer i = 1;
        FolderName name = FolderName.of("Shared");
        while (siblingFolderNames.contains(name)) {
            name = FolderName.of("Shared (" + ++i + ")");
        }

        final Folder folder = Folder.create(owner, name, rootFolder);
        final Acl acl = Acl.create(Resource.folder(folder), folder.getOwner());

        return new FolderCreationService.Result(acl, folder);

    }

    public record Result(Acl acl, Folder folder) {

    }

}
