package com.arqivame.drive.application.usecase.file.upload.authorize;

import java.util.UUID;

public record AuthorizeFileUploadInput(UUID fileId, UUID actor) {

}
