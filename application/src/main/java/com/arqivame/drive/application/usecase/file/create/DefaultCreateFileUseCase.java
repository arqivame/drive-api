package com.arqivame.drive.application.usecase.file.create;

import static java.util.Objects.requireNonNull;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Acl;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.exception.NotAllowedException;
import com.arqivame.drive.domain.exception.NotFoundException;
import com.arqivame.drive.domain.exception.QuotaExceededException;
import com.arqivame.drive.domain.exception.ValidationException;
import com.arqivame.drive.domain.file.Content;
import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileName;
import com.arqivame.drive.domain.file.gateway.command.FileCommandGateway;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;
import com.arqivame.drive.domain.member.Member;
import com.arqivame.drive.domain.member.MemberGateway;
import com.arqivame.drive.domain.member.MemberID;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.handler.Notification;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final EventDispatcher eventDispatcher;

    private final MemberGateway memberGateway;
    private final FolderQueryGateway folderQueryGateway;
    private final AclGateway aclGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;

    public DefaultCreateFileUseCase(
            final EventDispatcher eventDispatcher,
            final MemberGateway memberGateway,
            final FolderQueryGateway folderQueryGateway,
            final AclGateway aclGateway,
            final FileQueryGateway fileQueryGateway,
            final FileCommandGateway fileCommandGateway) {
        this.eventDispatcher = requireNonNull(eventDispatcher);
        this.memberGateway = requireNonNull(memberGateway);
        this.folderQueryGateway = requireNonNull(folderQueryGateway);
        this.aclGateway = requireNonNull(aclGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final MemberID creatorId = MemberID.of(input.creator());
        final FolderID folderId = FolderID.of(input.folder());
        final FileName name = FileName.of(input.name());
        final Content content = Content.of(input.contentType(), input.size());

        memberGateway
                .findById(creatorId)
                .orElseThrow(() -> NotFoundException.create(Member.class, creatorId));

        final Folder folder = folderQueryGateway
                .findVisibleById(folderId, creatorId)
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        final Acl folderAcl = aclGateway
                .findByResource(Resource.folder(folder))
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        final AccessPermission folderPermission = folderAcl
                .resolveAccessPermissionFor(creatorId)
                .orElseThrow(() -> NotFoundException.create(Folder.class, folderId));

        if (!folderPermission.isAtMost(AccessPermission.WRITE))
            throw NotAllowedException.with("You don't have permission to create files in this folder.");

        final Member owner = memberGateway
                .findById(folder.getOwner())
                .orElseThrow(() -> NotFoundException.create(Member.class, folder.getOwner()));

        checkQuota(owner, input.size());

        final Notification notification = Notification.create();
        name.validate(notification);
        content.validate(notification);
        if (notification.hasErrors())
            throw ValidationException.with("Could not create Aggregate File", notification);

        if (fileQueryGateway.existsFileWithNameInFolder(name, folderId))
            throw ValidationException.with("Could not create Aggregate File",
                    ValidationError.with("File with same name already exists on this folder"));

        final File file = notification.validate(() -> File.create(
                name,
                content,
                creatorId,
                folder.getOwner(),
                folderId));

        if (notification.hasErrors())
            throw ValidationException.with("Could not create Aggregate File", notification);

        final Acl fileAcl = folderAcl.deriveFor(Resource.file(file));

        eventDispatcher.notify(aclGateway.create(fileAcl));
        eventDispatcher.notify(fileCommandGateway.create(file));

        return new CreateFileOutput(null);
    }

    private void checkQuota(final Member owner, final Long newFileSize) {

        final Long usedQuota = fileQueryGateway.totalBytesUsedBy(owner.getId());

        if (usedQuota + newFileSize > owner.getQuota().sizeInBytes())
            throw QuotaExceededException.with(owner.getQuota());

    }

}
