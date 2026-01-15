package com.arqivame.drive.domain.folder;

import java.util.Optional;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.validation.ValidationHandler;

public class Folder extends AggregateRoot<FolderID> implements EventSource {

    private UserID owner;

    private Folder(FolderID id) {
        super(id);
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

    public UserID getOwner() {
        return owner;
    }

}
