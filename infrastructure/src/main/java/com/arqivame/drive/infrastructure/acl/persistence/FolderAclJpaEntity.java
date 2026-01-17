package com.arqivame.drive.infrastructure.acl.persistence;

import com.arqivame.drive.domain.acl.AccessPermission;
import com.arqivame.drive.domain.acl.Resource;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "FolderAcl")
@Table(name = "folder_acls")
public class FolderAclJpaEntity {

    @EmbeddedId
    private FolderAclID id;

    @Enumerated(EnumType.STRING)
    private AccessPermission effectiveAccessPermission;

    public FolderAclJpaEntity() {
    }

    private FolderAclJpaEntity(
            final FolderAclID id,
            final AccessPermission effectiveAccessPermission) {
        this.id = id;
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

    public static FolderAclJpaEntity from(
            final Resource<FolderID> resource,
            final UserID grantee,
            final AccessPermission effectiveAccessPermission) {
        return new FolderAclJpaEntity(
                FolderAclID.from(resource.getId().getValue(), grantee.getValue()),
                effectiveAccessPermission);
    }

    public FolderAclID getId() {
        return id;
    }

    public void setId(FolderAclID id) {
        this.id = id;
    }

    public AccessPermission getEffectiveAccessPermission() {
        return effectiveAccessPermission;
    }

    public void setEffectiveAccessPermission(AccessPermission effectiveAccessPermission) {
        this.effectiveAccessPermission = effectiveAccessPermission;
    }

}
