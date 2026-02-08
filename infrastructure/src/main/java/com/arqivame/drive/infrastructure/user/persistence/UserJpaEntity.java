package com.arqivame.drive.infrastructure.user.persistence;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.arqivame.drive.domain.user.Quota;
import com.arqivame.drive.domain.user.QuotaRequest;
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

    private Long requestedQuotaInBytes;

    private Long requestedQuotaAmount;

    @Enumerated(EnumType.STRING)
    private QuotaUnit requestedQuotaUnit;

    private Instant quotaRequestedAt;

    public UserJpaEntity(
            final UUID id,
            final Long quotaInBytes,
            final Long quotaAmount,
            final QuotaUnit quotaUnit,
            final Long requestedQuotaInBytes,
            final Long requestedQuotaAmount,
            final QuotaUnit requestedQuotaUnit,
            final Instant quotaRequestedAt) {
        this.id = id;
        this.quotaInBytes = quotaInBytes;
        this.quotaAmount = quotaAmount;
        this.quotaUnit = quotaUnit;
        this.requestedQuotaInBytes = requestedQuotaInBytes;
        this.requestedQuotaAmount = requestedQuotaAmount;
        this.requestedQuotaUnit = requestedQuotaUnit;
        this.quotaRequestedAt = quotaRequestedAt;
    }

    public UserJpaEntity() {
    }

    public static UserJpaEntity fromDomain(final User user) {
        return new UserJpaEntity(
                user.getId().getValue(),
                user.getQuota().sizeInBytes(),
                user.getQuota().amount(),
                user.getQuota().unit(),
                user.getQuotaRequest().map(QuotaRequest::requestedQuota).map(Quota::sizeInBytes).orElse(null),
                user.getQuotaRequest().map(QuotaRequest::requestedQuota).map(Quota::amount).orElse(null),
                user.getQuotaRequest().map(QuotaRequest::requestedQuota).map(Quota::unit).orElse(null),
                user.getQuotaRequest().map(QuotaRequest::requesteddAt).orElse(null));
    }

    public User toDomain() {

        QuotaRequest quotaRequest = null;
        if (getRequestedQuotaAmount() != null && getRequestedQuotaUnit() != null && getQuotaRequestedAt() != null)
            quotaRequest = QuotaRequest.of(
                    Quota.of(getRequestedQuotaAmount(), getRequestedQuotaUnit()),
                    getQuotaRequestedAt());

        return User.with(
                UserID.of(id),
                Quota.of(quotaAmount, quotaUnit),
                Optional.ofNullable(quotaRequest),
                null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getQuotaInBytes() {
        return quotaInBytes;
    }

    public void setQuotaInBytes(Long quotaInBytes) {
        this.quotaInBytes = quotaInBytes;
    }

    public Long getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(Long quotaAmount) {
        this.quotaAmount = quotaAmount;
    }

    public QuotaUnit getQuotaUnit() {
        return quotaUnit;
    }

    public void setQuotaUnit(QuotaUnit quotaUnit) {
        this.quotaUnit = quotaUnit;
    }

    public Long getRequestedQuotaInBytes() {
        return requestedQuotaInBytes;
    }

    public void setRequestedQuotaInBytes(Long requestedQuotaInBytes) {
        this.requestedQuotaInBytes = requestedQuotaInBytes;
    }

    public Long getRequestedQuotaAmount() {
        return requestedQuotaAmount;
    }

    public void setRequestedQuotaAmount(Long requestedQuotaAmount) {
        this.requestedQuotaAmount = requestedQuotaAmount;
    }

    public QuotaUnit getRequestedQuotaUnit() {
        return requestedQuotaUnit;
    }

    public void setRequestedQuotaUnit(QuotaUnit requestedQuotaUnit) {
        this.requestedQuotaUnit = requestedQuotaUnit;
    }

    public Instant getQuotaRequestedAt() {
        return quotaRequestedAt;
    }

    public void setQuotaRequestedAt(Instant quotaRequestedAt) {
        this.quotaRequestedAt = quotaRequestedAt;
    }

}
