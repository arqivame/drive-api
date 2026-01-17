package com.arqivame.drive.infrastructure.acl.persistence;

import java.util.UUID;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.file.FileID;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FileAcl")
@Table(name = "file_acls")
public class FileAclJpaEntity {

    @EmbeddedId
    private FileAclID id;

    @Column(name = "file_id", insertable = false, updatable = false)
    private UUID fileId;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    public FileAclJpaEntity() {
    }

    private FileAclJpaEntity(
            final FileAclID id,
            final AccessPermission effectiveAccessPermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

    public static FileAclJpaEntity from(
            final Resource<FileID> resource,
            final UserID grantee,
            final AccessPermission effectiveAccessPermission) {
        return new FileAclJpaEntity(
                FileAclID.from(resource.getId().getValue(), grantee.getValue()),
                effectiveAccessPermission);
    }

    public FileAclID getId() {
        return id;
    }

    public void setId(FileAclID id) {
        this.id = id;
    }

    public AccessPermission getEffectiveAccessPermission() {
        return effectiveAccessPermission;
    }

    public void setEffectiveAccessPermission(AccessPermission effectiveAccessPermission) {
        this.effectiveAccessPermission = effectiveAccessPermission;
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
        FileAclJpaEntity other = (FileAclJpaEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
