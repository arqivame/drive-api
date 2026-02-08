package com.arqivame.drive.application.usecase.folder.create;

import com.arqivame.drive.application.service.folder.FolderProvisioningService;
import com.arqivame.drive.domain.exception.NotFoundException;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.FolderName;
import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final FolderProvisioningService folderProvisioningService;

    private final UserQueryGateway userQueryGateway;

    public DefaultCreateFolderUseCase(
            final FolderProvisioningService folderProvisioningService,
            final UserQueryGateway userQueryGateway) {
        this.folderProvisioningService = folderProvisioningService;
        this.userQueryGateway = userQueryGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {

        final UserID creatorId = UserID.of(input.creatorId());
        final FolderID parentId = FolderID.of(input.parentId());
        final FolderName name = FolderName.of(input.name());

        if (!userQueryGateway.existsById(creatorId))
            throw NotFoundException.create(User.class, creatorId);

        final Folder folder = folderProvisioningService.folder(parentId, creatorId, name);

        return CreateFolderOutput.from(folder);
    }

}
