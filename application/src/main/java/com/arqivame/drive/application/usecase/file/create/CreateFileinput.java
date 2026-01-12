package com.arqivame.drive.application.usecase.file.create;

import java.util.UUID;

public record CreateFileinput(
        UUID creator,
        UUID folder,
        String name,
        String contentType,
        Long size) {

}
