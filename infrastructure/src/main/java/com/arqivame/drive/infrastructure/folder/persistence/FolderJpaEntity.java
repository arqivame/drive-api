package com.arqivame.drive.infrastructure.folder.persistence;

import java.time.Instant;
import java.util.UUID;

import com.arqivame.drive.domain.folder.Folder;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.folder.FolderName;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Folder")
@Table(name = "folders")
public class FolderJpaEntity {

    @Id
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "createdAt", nullable = false)
    private Instant createdAt;

    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;

    @Column(name = "deletedAt")
    private Instant deletedAt;

    @Column(name = "is_root", nullable = false)
    private Boolean isRoot;

    public FolderJpaEntity() {
    }

    private FolderJpaEntity(
            final UUID id,
            final UUID ownerId,
            final UUID creatorId,
            final String name,
            final UUID parentId,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deleteAt,
            final Boolean isRoot) {
        this.id = id;
        this.ownerId = ownerId;
        this.creatorId = creatorId;
        this.name = name;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deleteAt;
    }

    public static FolderJpaEntity fromDomain(final Folder folder) {
        return new FolderJpaEntity(
                folder.getId().getValue(),
                folder.getOwner().getValue(),
                folder.getCreator().getValue(),
                folder.getName().value(),
                folder.getParent().getValue(),
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folder.getDeletedAt(),
                folder.isRoot());
    }

    public Folder toDomain() {
        return Folder.with(
                FolderID.of(id),
                UserID.of(creatorId),
                UserID.of(ownerId),
                FolderName.of(name),
                FolderID.of(parentId),
                createdAt,
                updatedAt,
                deletedAt);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
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

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
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
        FolderJpaEntity other = (FolderJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
