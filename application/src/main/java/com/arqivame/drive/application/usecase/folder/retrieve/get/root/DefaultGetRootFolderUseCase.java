package com.arqivame.drive.application.usecase.folder.retrieve.get.root;

import java.util.Objects;

import com.arqivame.drive.application.service.folder.FolderProvisioningService;
import com.arqivame.drive.domain.file.gateway.query.FileQueryGateway;
import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.gateway.query.FolderQueryGateway;
import com.arqivame.drive.domain.user.UserID;

public class DefaultGetRootFolderUseCase extends GetRootFolderUseCase {

    private final FolderQueryGateway folderQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final FolderProvisioningService folderProvisioningService;

    public DefaultGetRootFolderUseCase(
            final FolderQueryGateway folderQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final FolderProvisioningService folderProvisioningService) {
        this.folderQueryGateway = Objects.requireNonNull(folderQueryGateway);
        this.fileQueryGateway = Objects.requireNonNull(fileQueryGateway);
        this.folderProvisioningService = Objects.requireNonNull(folderProvisioningService);
    }

    @Override
    public GetRootFolderOutput execute(final GetRootFolderInput input) {

        final UserID owner = UserID.of(input.ownerId());

        final Folder rootFolder = this.folderQueryGateway
                .findRootByOwner(owner)
                .orElseGet(() -> this.folderProvisioningService.rootFolder(owner));

        return GetRootFolderOutput.from(
                rootFolder,
                this.folderQueryGateway.findByParentFolderId(rootFolder.getId()),
                this.fileQueryGateway.findAllInFolder(rootFolder.getId()));

    }

}
