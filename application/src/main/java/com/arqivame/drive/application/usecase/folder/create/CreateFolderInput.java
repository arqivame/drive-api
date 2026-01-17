package com.arqivame.drive.application.usecase.folder.create;

import java.util.UUID;

public record CreateFolderInput(
        UUID creatorId,
        UUID parentId,
        String name) {

}
