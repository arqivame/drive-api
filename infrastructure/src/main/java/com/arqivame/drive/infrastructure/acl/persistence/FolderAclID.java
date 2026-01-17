package com.arqivame.drive.infrastructure.acl.persistence;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FolderAclID implements Serializable {

    @Column(name = "folder_id", nullable = false)
    private UUID folderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public FolderAclID() {
    }

    private FolderAclID(UUID folderId, UUID userId) {
        this.folderId = folderId;
        this.userId = userId;
    }

    public static FolderAclID from(final UUID folderId, final UUID userId) {
        return new FolderAclID(folderId, userId);
    }

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((folderId == null) ? 0 : folderId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        FolderAclID other = (FolderAclID) obj;
        if (folderId == null) {
            if (other.folderId != null)
                return false;
        } else if (!folderId.equals(other.folderId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
