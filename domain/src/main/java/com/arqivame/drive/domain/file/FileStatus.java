package com.arqivame.drive.domain.file;

public enum FileStatus {
    NEW,
    UPLOAD_AUTHORIZED,
    UPLOADING,
    UPLOAD_COMPLETED,
    UPLOAD_ABORTED,
    PROCESSING,
    AVAILABLE,
    FAILED;
}
