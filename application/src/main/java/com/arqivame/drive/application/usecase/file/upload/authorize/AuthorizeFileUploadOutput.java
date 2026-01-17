package com.arqivame.drive.application.usecase.file.upload.authorize;

import java.time.Instant;
import java.util.UUID;

public record AuthorizeFileUploadOutput(
        UUID fileId,
        Long fileSize,
        Integer maxChunksAtSameTime,
        Long maxBytesPerSecondTransferRatePerChunk,
        Instant expiresAt) {

}
