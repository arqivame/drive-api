package com.arqivame.drive.domain.folder;

import static java.util.Objects.isNull;

import java.time.Instant;
import java.util.Optional;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationHandler;

public class Folder extends AggregateRoot<FolderID> implements EventSource {

    private final UserID creator;
    private final UserID owner;
    private FolderName name;
    private FolderID parent;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Folder(
            final FolderID id,
            final UserID creator,
            final UserID owner,
            final FolderName name,
            final FolderID parent,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id);
        this.owner = owner;
        this.creator = creator;
        this.name = name;
        this.parent = parent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Folder with(
            final FolderID id,
            final UserID creator,
            final UserID owner,
            final FolderName name,
            final FolderID parent,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Folder(id, creator, owner, name, parent, createdAt, updatedAt, deletedAt);
    }

    public static Folder create(
            final UserID creator,
            final FolderName name,
            final Folder parent) {

        final Instant now = Instant.now();

        final Folder folder = new Folder(
                FolderID.unique(),
                creator,
                parent.getOwner(),
                name, parent.getId(),
                now,
                now,
                null);

        return folder;
    }

    public static Folder createRoot(final UserID creator) {

        final Instant now = Instant.now();

        final Folder folder = new Folder(
                FolderID.unique(),
                creator,
                creator,
                FolderName.of("root"),
                null,
                now,
                now,
                null);

        // folder.events.add(FolderCreatedEvent.create(folder));

        return folder;

    }

    public Boolean isRoot() {
        return isNull(parent);
    }

    @Override
    public void validate(ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextEvent'");
    }

    public UserID getCreator() {
        return creator;
    }

    public UserID getOwner() {
        return owner;
    }

    public FolderName getName() {
        return name;
    }

    public FolderID getParent() {
        return parent;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

}
