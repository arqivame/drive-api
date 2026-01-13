package com.arqivame.drive.domain.file;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.exception.InvalidStateException;
import com.arqivame.drive.domain.folder.FolderID;
import com.arqivame.drive.domain.member.MemberID;
import com.arqivame.drive.domain.validation.ValidationHandler;
import com.arqivame.drive.domain.validation.handler.Notification;

public class File extends AggregateRoot<FileID> implements EventSource {

    private FileName name;
    private Content content;

    private final MemberID creator;
    private final MemberID owner;

    private FolderID folder;
    private final Set<FileSharing> sharings;

    private final Queue<Event<?>> events;

    private File(
            final FileID id,
            final FileName name,
            final Content content,
            final MemberID creator,
            final MemberID owner,
            final FolderID folder,
            final Set<FileSharing> sharings,
            final Queue<Event<?>> events) {
        super(id);
        this.name = requireNonNull(name);
        this.content = requireNonNull(content);
        this.creator = requireNonNull(creator);
        this.owner = requireNonNull(owner);
        this.folder = requireNonNull(folder);
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
            MemberID creator,
            MemberID owner,
            FolderID folder) {
        return new File(
                FileID.unique(),
                name,
                content,
                creator,
                owner,
                folder,
                null,
                null);
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        return Optional.ofNullable(this.events.poll());
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        validate(notification);
        if (notification.hasErrors())
            throw InvalidStateException.with(File.class, notification.getDomainErrors());
    }

    public FileName getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public MemberID getCreator() {
        return creator;
    }

    public MemberID getOwner() {
        return owner;
    }

    public FolderID getFolder() {
        return folder;
    }

    public Set<FileSharing> getSharings() {
        return sharings;
    }

    public Queue<Event<?>> getEvents() {
        return new LinkedList<>(events);
    }

}
