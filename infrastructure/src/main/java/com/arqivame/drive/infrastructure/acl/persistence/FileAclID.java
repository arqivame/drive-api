package com.arqivame.drive.infrastructure.acl.persistence;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class FileAclID implements Serializable {

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public FileAclID() {
    }

    private FileAclID(UUID fileId, UUID userId) {
        this.fileId = fileId;
        this.userId = userId;
    }

    public static FileAclID from(final UUID fileId, final UUID userId) {
        return new FileAclID(fileId, userId);
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileAclID other = (FileAclID) obj;
        if (fileId == null) {
            if (other.fileId != null)
                return false;
        } else if (!fileId.equals(other.fileId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

}
