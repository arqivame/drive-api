package com.arqivame.drive.application.usecase.folder.create;

import com.arqivame.drive.application.service.folder.FolderProvisioningService;
import com.arqivame.drive.domain.exception.NotFoundException;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.FolderName;
import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserGateway;
import com.arqivame.drive.domain.user.UserID;

public class DefaultCreateFolderUseCase extends CreateFolderUseCase {

    private final FolderProvisioningService folderProvisioningService;

    private final UserGateway userGateway;

    public DefaultCreateFolderUseCase(
            final FolderProvisioningService folderProvisioningService,
            final UserGateway userGateway) {
        this.folderProvisioningService = folderProvisioningService;
        this.userGateway = userGateway;
    }

    @Override
    public CreateFolderOutput execute(final CreateFolderInput input) {

        final UserID creatorId = UserID.of(input.creatorId());
        final FolderID parentId = FolderID.of(input.parentId());
        final FolderName name = FolderName.of(input.name());

        if (!userGateway.existsById(creatorId))
            throw NotFoundException.create(User.class, creatorId);

        final Folder folder = folderProvisioningService.folder(parentId, creatorId, name);

        return CreateFolderOutput.from(folder);
    }

}
