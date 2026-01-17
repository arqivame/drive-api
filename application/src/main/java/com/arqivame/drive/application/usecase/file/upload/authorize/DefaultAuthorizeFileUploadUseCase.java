package com.arqivame.drive.application.usecase.file.upload.authorize;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Acl;
import com.arqivame.drive.domain.acl.AclGateway;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.exception.NotAllowedException;
import com.arqivame.drive.domain.exception.NotFoundException;
import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.file.gateway.command.FileCommandGateway;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserGateway;
import com.arqivame.drive.domain.user.UserID;

public class DefaultAuthorizeFileUploadUseCase extends AuthorizeFileUploadUseCase {

    private final EventDispatcher eventDispatcher;

    private final UserGateway userGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FileCommandGateway fileCommandGateway;
    private final AclGateway aclGateway;

    public DefaultAuthorizeFileUploadUseCase(
            final EventDispatcher eventDispatcher,
            final UserGateway userGateway,
            final FileQueryGateway fileQueryGateway,
            final FileCommandGateway fileCommandGateway,
            final AclGateway aclGateway) {
        this.eventDispatcher = requireNonNull(eventDispatcher);
        this.userGateway = requireNonNull(userGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.fileCommandGateway = requireNonNull(fileCommandGateway);
        this.aclGateway = requireNonNull(aclGateway);
    }

    @Override
    public AuthorizeFileUploadOutput execute(final AuthorizeFileUploadInput input) {

        final UserID actorId = UserID.of(input.actor());
        final FileID fileId = FileID.of(input.fileId());

        final User actor = userGateway
                .findById(actorId)
                .orElseThrow(() -> NotFoundException.create(User.class, actorId));

        final File file = fileQueryGateway
                .findVisibleById(fileId, actorId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final Acl fileAcl = aclGateway
                .findByResource(Resource.file(file))
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final AccessPermission filePermission = fileAcl
                .resolveAccessPermissionFor(actorId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        if (!filePermission.isAtMost(AccessPermission.WRITE))
            throw NotAllowedException.with(
                    "User does not have write permission for the file.",
                    "File [%s], User [%s]".formatted(fileId.getValue(), actorId.getValue()));

        eventDispatcher.notify(fileCommandGateway.update(file.authorizeUpload()));

        // TODO planos premium para ter mais banda e mais chunks
        return new AuthorizeFileUploadOutput(
                file.getId().getValue(),
                file.getContent().size(),
                2,
                1024L * 1024L * 5L, // 5 MB/s
                Instant.now().plusSeconds(3600)); // 1 hour

    }

}
