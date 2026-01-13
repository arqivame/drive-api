package com.arqivame.drive.domain.acl;

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
import com.arqivame.drive.domain.member.MemberID;
import com.arqivame.drive.domain.validation.ValidationHandler;

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

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
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

    public Optional<AccessPermission> resolveAccessPermissionFor(final MemberID member) {

        if (this.resource.getOwner().equals(member))
            return Optional.of(AccessPermission.MANAGE);

        return Stream.concat(directEntries.stream(), inheritedEntries.stream())
                .filter(entry -> entry.member().equals(member))
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
