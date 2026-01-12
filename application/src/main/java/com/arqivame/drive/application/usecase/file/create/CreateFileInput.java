package com.arqivame.drive.application.usecase.file.create;

import java.util.UUID;

public record CreateFileInput(
        UUID creator,
        UUID folder,
        String name,
        String contentType,
        Long size) {

}
