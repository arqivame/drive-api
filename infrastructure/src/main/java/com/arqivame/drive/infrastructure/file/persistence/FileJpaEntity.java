package com.arqivame.drive.infrastructure.file.persistence;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.arqivame.drive.domain.file.Content;
import com.arqivame.drive.domain.file.File;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.file.FileName;
import com.arqivame.drive.domain.file.FileSharing;
import com.arqivame.drive.domain.file.FileStatus;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "File")
@Table(name = "files")
public class FileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "content_size", nullable = false)
    private Long contentSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileStatus status;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "folder_id", nullable = false)
    private UUID folderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public FileJpaEntity() {
    }

    private FileJpaEntity(
            final UUID id,
            final String name,
            final String contentType,
            final Long contentSize,
            final FileStatus status,
            final UUID creator,
            final UUID owner,
            final UUID folder,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
        this.contentSize = contentSize;
        this.status = status;
        this.creatorId = creator;
        this.ownerId = owner;
        this.folderId = folder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static FileJpaEntity fromDomain(final File file) {
        return new FileJpaEntity(
                file.getId().getValue(),
                file.getName().value(),
                file.getContent().type(),
                file.getContent().size(),
                file.getStatus(),
                file.getCreator().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt());
    }

    public File toDomain(final Set<FileSharing> fileSharings) {
        return File.with(
                FileID.of(this.id),
                FileName.of(this.name),
                Content.of(this.contentType, this.contentSize),
                this.status,
                UserID.of(this.creatorId),
                UserID.of(this.ownerId),
                FolderID.of(this.folderId),
                fileSharings,
                this.createdAt,
                this.updatedAt,
                this.deletedAt,
                null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentSize() {
        return contentSize;
    }

    public void setContentSize(Long contentSize) {
        this.contentSize = contentSize;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

}
