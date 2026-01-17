package com.arqivame.drive.infrastructure.acl.persistence;

import java.time.Instant;
import java.util.UUID;

import com.arqivame.drive.domain.acl.Entry;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Entry")
@Table(name = "acl_entries")
public class EntryJpaEntity {

    @Id
    private EntryJpaID id;

    @Column(name = "acl_id", insertable = false, updatable = false)
    private UUID aclId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    private Type type;

    @Column(name = "granted_at", nullable = false)
    private Instant grantedAt;

    public enum Type {
        DIRECT,
        INHERITED
    }

    private EntryJpaEntity(
            final EntryJpaID id,
            final UUID aclId,
            final Type type,
            final Instant grantedAt) {
        this.id = id;
        this.aclId = aclId;
        this.type = type;
        this.grantedAt = grantedAt;
    }

    public EntryJpaEntity() {
    }

    public static EntryJpaEntity fromDomain(
            final AclJpaEntity aclJpa,
            final EntryJpaEntity.Type type,
            final Entry entry) {
        return new EntryJpaEntity(
                new EntryJpaID(aclJpa.getId(), entry.user().getValue(), entry.permission(), type),
                aclJpa.getId(),
                type,
                entry.grantedAt());
    }

    public Entry toDomain() {
        return new Entry(
                UserID.of(id.getUserId()),
                this.id.getAccessPermission(),
                this.grantedAt);
    }

    public EntryJpaID getId() {
        return id;
    }

    public void setId(EntryJpaID id) {
        this.id = id;
    }

    public UUID getAclId() {
        return aclId;
    }

    public void setAclId(UUID aclId) {
        this.aclId = aclId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Instant getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Instant grantedAt) {
        this.grantedAt = grantedAt;
    }

}
