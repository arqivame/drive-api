package com.arqivame.drive.application.usecase.folder.create;

import java.util.UUID;

import com.arqivame.drive.domain.folder.Folder;

public record CreateFolderOutput(UUID id) {

    public static CreateFolderOutput from(final Folder folder) {
        return new CreateFolderOutput(folder.getId().getValue());
    }

}
