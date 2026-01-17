package com.arqivame.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.UUID;

import com.arqivame.drive.domain.file.FileSharing;
import com.arqivame.drive.domain.file.FileSharingID;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "FileSharing")
@Table(name = "file_sharings")
public class FileSharingJpaEntity {

    @Id
    private UUID id;

    private UUID sharedTo;

    private UUID sharedBy;

    private UUID virtualFolderId;

    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FileJpaEntity file;

    public FileSharingJpaEntity() {
    }

    private FileSharingJpaEntity(
            final UUID id,
            final UUID sharedTo,
            final UUID sharedBy,
            final UUID virtualFolderId,
            final Instant createdAt,
            final FileJpaEntity file) {
        this.id = id;
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.virtualFolderId = virtualFolderId;
        this.createdAt = createdAt;
        this.file = file;
    }

    public static FileSharingJpaEntity from(final FileJpaEntity fileJpaEntity, final FileSharing sharing) {
        return new FileSharingJpaEntity(
                sharing.getId().getValue(),
                sharing.getSharedTo().getValue(),
                sharing.getSharedBy().getValue(),
                sharing.getVirtualFolder().getValue(),
                sharing.getCreatedAt(),
                fileJpaEntity);
    }

    public FileSharing toDomain() {
        return FileSharing.with(
                FileSharingID.of(this.id),
                UserID.of(this.sharedTo),
                UserID.of(this.sharedBy),
                FolderID.of(this.virtualFolderId),
                this.createdAt);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(UUID sharedTo) {
        this.sharedTo = sharedTo;
    }

    public UUID getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(UUID sharedBy) {
        this.sharedBy = sharedBy;
    }

    public UUID getVirtualFolderId() {
        return virtualFolderId;
    }

    public void setVirtualFolderId(UUID virtualFolderId) {
        this.virtualFolderId = virtualFolderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public FileJpaEntity getFile() {
        return file;
    }

    public void setFile(FileJpaEntity file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileSharingJpaEntity other = (FileSharingJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
