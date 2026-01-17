package com.arqivame.drive.infrastructure.acl.persistence;

import java.io.Serializable;
import java.util.UUID;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.infrastructure.acl.persistence.EntryJpaEntity.Type;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@Access(AccessType.FIELD)
public class EntryJpaID implements Serializable {

    @Column(name = "acl_id", nullable = false)
    private UUID aclId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_permission", nullable = false)
    private AccessPermission accessPermission;

    @Column(name = "type", nullable = false)
    private EntryJpaEntity.Type type;

    public EntryJpaID(
            final UUID aclId,
            final UUID userId,
            final AccessPermission accessPermission,
            final Type type) {
        this.aclId = aclId;
        this.userId = userId;
        this.accessPermission = accessPermission;
        this.type = type;
    }

    public EntryJpaID() {
    }

    public UUID getAclId() {
        return aclId;
    }

    public void setAclId(UUID aclId) {
        this.aclId = aclId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public AccessPermission getAccessPermission() {
        return accessPermission;
    }

    public void setAccessPermission(AccessPermission accessPermission) {
        this.accessPermission = accessPermission;
    }

    public EntryJpaEntity.Type getType() {
        return type;
    }

    public void setType(EntryJpaEntity.Type type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((aclId == null) ? 0 : aclId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((accessPermission == null) ? 0 : accessPermission.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        EntryJpaID other = (EntryJpaID) obj;
        if (aclId == null) {
            if (other.aclId != null)
                return false;
        } else if (!aclId.equals(other.aclId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (accessPermission != other.accessPermission)
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
