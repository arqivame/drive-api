package com.arqivame.drive.domain.file;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.exception.InvalidStateException;
import com.arqivame.drive.domain.exception.DomainException.Error;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationHandler;
import com.arqivame.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private FileName name;
    private Content content;

    private FileStatus status;

    private final UserID creator;
    private final UserID owner;

    private FolderID folder;
    private final Set<FileSharing> sharings;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final FileName name,
            final Content content,
            final FileStatus status,
            final UserID creator,
            final UserID owner,
            final FolderID folder,
            final Set<FileSharing> sharings,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Queue<Event<?>> events) {
        super(id);
        this.name = requireNonNull(name);
        this.content = requireNonNull(content);
        this.status = requireNonNull(status);
        this.creator = requireNonNull(creator);
        this.owner = requireNonNull(owner);
        this.folder = requireNonNull(folder);
        this.createdAt = requireNonNull(createdAt);
        this.updatedAt = requireNonNull(updatedAt);
        this.deletedAt = deletedAt;
        this.sharings = isNull(sharings) ? new HashSet<>() : new HashSet<>(sharings);

        this.events = isNull(events) ? new LinkedList<>() : new LinkedList<>(events);

        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {

    }

    public static File create(
            FileName name,
            Content content,
            UserID creator,
            UserID owner,
            FolderID folder) {

        final Instant now = Instant.now();

        final File file = new File(
                FileID.unique(),
                name,
                content,
                FileStatus.NEW,
                creator,
                owner,
                folder,
                null,
                now,
                now,
                null,
                null);

        return file;
    }

    public static File with(
            final FileID id,
            final FileName name,
            final Content content,
            final FileStatus status,
            final UserID creator,
            final UserID owner,
            final FolderID folder,
            final Set<FileSharing> sharings,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final Queue<Event<?>> events) {
        return new File(
                id,
                name,
                content,
                status,
                creator,
                owner,
                folder,
                sharings,
                createdAt,
                updatedAt,
                deletedAt,
                events);
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    public File authorizeUpload() {

        if (FileStatus.UPLOAD_AUTHORIZED.equals(this.status))
            return this;

        if (FileStatus.AVAILABLE.equals(this.status))
            throw InvalidStateException.with(File.class, Error.with("File is already available."));

        this.status = FileStatus.UPLOAD_AUTHORIZED;

        return this;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(File.class, notification.getDomainErrors());
    }

    public Queue<Event<?>> getEvents() {
        return new LinkedList<>(events);
    }

    public FileName getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public FileStatus getStatus() {
        return status;
    }

    public UserID getCreator() {
        return creator;
    }

    public UserID getOwner() {
        return owner;
    }

    public FolderID getFolder() {
        return folder;
    }

    public Set<FileSharing> getSharings() {
        return sharings;
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
