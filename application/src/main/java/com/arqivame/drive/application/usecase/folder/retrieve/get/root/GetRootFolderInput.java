package com.arqivame.drive.application.usecase.folder.retrieve.get.root;

import java.util.UUID;

public record GetRootFolderInput(UUID ownerId) {

    public static GetRootFolderInput from(final UUID ownerId) {
        return new GetRootFolderInput(ownerId);
    }

}
