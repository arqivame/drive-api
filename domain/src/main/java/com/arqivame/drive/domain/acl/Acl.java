package com.arqivame.drive.domain.acl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.exception.ValidationException;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;
import com.arqivame.drive.domain.validation.handler.Notification;

public class Acl extends AggregateRoot<AclID> implements EventSource {

    private final Resource<?> resource;
    private final Set<Entry> directEntries;
    private final Set<Entry> inheritedEntries;

    private final Instant createdAt;
    private Instant updatedAt;

    public Acl(
            final AclID id,
            final Resource<?> resource,
            final Set<Entry> directEntries,
            final Set<Entry> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt) {
        super(id);
        this.resource = resource;
        this.directEntries = nonNull(directEntries) ? new HashSet<>(directEntries) : new HashSet<>();
        this.inheritedEntries = nonNull(inheritedEntries) ? new HashSet<>(inheritedEntries) : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Acl with(
            final AclID id,
            final Resource<?> resource,
            final Set<Entry> directEntries,
            final Set<Entry> inheritedEntries,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Acl(id, resource, directEntries, inheritedEntries, createdAt, updatedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public static Acl create(final Resource<?> resource, final UserID owner) {

        Instant now = Instant.now();

        final Acl newAcl = new Acl(
                AclID.unique(),
                resource,
                null,
                null,
                now,
                now)
                .grantTotal(owner);

        return newAcl;
    }

    public Acl deriveFor(final Resource<?> resource) {

        final Instant now = Instant.now();

        final Set<Entry> inheritedEntries = inheritEntries(this);

        final Acl newAcl = new Acl(
                AclID.unique(),
                resource,
                null,
                inheritedEntries,
                now,
                now);

        return newAcl;
    }

    public Optional<AccessPermission> resolveAccessPermissionFor(final UserID user) {

        if (this.resource.getOwner().equals(user))
            return Optional.of(AccessPermission.MANAGE);

        return Stream.concat(directEntries.stream(), inheritedEntries.stream())
                .filter(entry -> entry.user().equals(user))
                .map(Entry::permission)
                .filter(AccessPermission.class::isInstance)
                .map(AccessPermission.class::cast)
                .min((e1, e2) -> e1.level().compareTo(e2.level()));
    }

    private static Set<Entry> inheritEntries(final Acl acl) {
        return Stream
                .concat(acl.directEntries.stream(), acl.inheritedEntries.stream())
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toSet());
    }

    private Acl applyEntry(
            final UserID grantee,
            final AccessPermission permission) {

        final Notification notification = Notification.create();

        if (isNull(grantee))
            notification.append(ValidationError.with("'grantee' should not be null"));
        if (isNull(permission))
            notification.append(ValidationError.with("'permission' should not be null"));

        if (notification.hasErrors())
            throw ValidationException.with("Could not apply entry", notification);

        final Entry entry = Entry.create(grantee, permission);

        if (this.directEntries.stream().anyMatch(e -> e.isEquivalentTo(entry)))
            return this;

        this.directEntries.add(entry);

        return this;

    }

    private Acl grantTotal(final UserID grantee) {
        return this.applyEntry(grantee, AccessPermission.mostPrivileged());
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextEvent'");
    }

    public Resource<?> getResource() {
        return resource;
    }

    public Set<Entry> getDirectEntries() {
        return Set.copyOf(directEntries);
    }

    public Set<Entry> getInheritedEntries() {
        return Set.copyOf(inheritedEntries);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
