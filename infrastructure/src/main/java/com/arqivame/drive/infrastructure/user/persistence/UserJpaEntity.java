package com.arqivame.drive.infrastructure.user.persistence;

import java.util.UUID;

import com.arqivame.drive.domain.user.Quota;
import com.arqivame.drive.domain.user.QuotaUnit;
import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "User")
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long quotaInBytes;

    @Column(nullable = false)
    private Long quotaAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaUnit quotaUnit;

    public UserJpaEntity() {
    }

    private UserJpaEntity(
            final UUID id,
            final Long quotaInBytes,
            final Long quotaAmount,
            final QuotaUnit quotaUnit) {
        this.id = id;
        this.quotaInBytes = quotaInBytes;
        this.quotaAmount = quotaAmount;
        this.quotaUnit = quotaUnit;
    }

    public static UserJpaEntity fromDomain(final User user) {
        return new UserJpaEntity(
                user.getId().getValue(),
                user.getQuota().sizeInBytes(),
                user.getQuota().amount(),
                user.getQuota().unit());
    }

    public User toDomain() {
        return User.with(
                UserID.of(id),
                Quota.of(quotaAmount, quotaUnit));
    }

}
