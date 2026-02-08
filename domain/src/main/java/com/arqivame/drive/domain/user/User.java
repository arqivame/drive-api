package com.arqivame.drive.domain.user;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.exception.InvalidStateException;
import com.arqivame.drive.domain.exception.ValidationException;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;
import com.arqivame.drive.domain.validation.handler.Notification;

public class User extends AggregateRoot<UserID> implements EventSource {

    private Quota quota;
    private Optional<QuotaRequest> quotaRequest;

    private final Queue<Event<?>> events;

    private User(
            final UserID id,
            final Quota quota,
            final Optional<QuotaRequest> quotaRequest,
            final Queue<Event<?>> events) {
        super(id);
        this.quota = quota;
        this.quotaRequest = quotaRequest;

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();

    }

    public static User with(
            final UserID id,
            final Quota quota,
            final Optional<QuotaRequest> quotaRequest,
            final Queue<Event<?>> events) {
        return new User(id, quota, quotaRequest, events);
    }

    public static User create(final UserID id) {
        return new User(
                id,
                Quota.of(0L, QuotaUnit.BYTE),
                Optional.empty(),
                null);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (quota == null)
            handler.append(ValidationError.with("'quota' should not be null"));

        if (quotaRequest != null)
            quotaRequest.ifPresent(q -> q.validate(handler));

    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(User.class, notification.getDomainErrors());
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public User requestQuota(final Quota quota) {

        final QuotaRequest newQuotaRequest = QuotaRequest.of(quota, Instant.now());

        final Notification notification = Notification.create();
        newQuotaRequest.validate(notification);

        if (notification.hasErrors())
            throw ValidationException.with("Request Quota Error", notification);

        this.quotaRequest = Optional.ofNullable(newQuotaRequest);
        return this;

    }

    public User approveQuotaRequest() {

        if (this.quotaRequest.isEmpty())
            return this;

        this.quota = this.quotaRequest
                .map(QuotaRequest::requestedQuota)
                .orElse(Quota.of(0L, QuotaUnit.BYTE));

        this.quotaRequest = Optional.empty();

        return this;
    }

    public User reproveQuotaRequest() {

        if (this.quotaRequest.isEmpty())
            return this;

        this.quotaRequest = Optional.empty();

        return this;
    }

    public Quota getQuota() {
        return quota;
    }

    public Optional<QuotaRequest> getQuotaRequest() {
        return quotaRequest;
    }

}
