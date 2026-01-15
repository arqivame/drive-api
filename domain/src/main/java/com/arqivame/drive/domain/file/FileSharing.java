package com.arqivame.drive.domain.file;

import java.time.Instant;

import com.arqivame.drive.domain.Entity;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationHandler;

public class FileSharing extends Entity<FileSharingID> {

    private final UserID sharedTo;
    private final UserID sharedBy;
    private final FolderID virtualFolder;
    private final Instant createdAt;

    private FileSharing(
            final FileSharingID id,
            final UserID sharedTo,
            final UserID sharedBy,
            final FolderID virtualFolder,
            final Instant createdAt) {
        super(id);
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolder = virtualFolder;
        this.createdAt = createdAt;
    }

    public static FileSharing with(
            final FileSharingID id,
            final UserID sharedTo,
            final UserID sharedBy,
            final FolderID virtualFolder,
            final Instant createdAt) {
        return new FileSharing(
                id,
                sharedTo,
                sharedBy,
                virtualFolder,
                createdAt);
    }

    public static FileSharing create(
            final UserID sharedTo,
            final UserID sharedBy,
            final FolderID virtualFolder) {
        final Instant now = Instant.now();
        return new FileSharing(
                FileSharingID.unique(),
                sharedTo,
                sharedBy,
                virtualFolder,
                now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
    }

    public UserID getSharedTo() {
        return sharedTo;
    }

    public UserID getSharedBy() {
        return sharedBy;
    }

    public FolderID getVirtualFolder() {
        return virtualFolder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
