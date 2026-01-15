package com.arqivame.drive.domain.user;

import java.util.Optional;

import com.arqivame.drive.domain.AggregateRoot;
import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventSource;
import com.arqivame.drive.domain.validation.ValidationHandler;

public class User extends AggregateRoot<UserID> implements EventSource {

    private Quota quota;

    private User(UserID id) {
        super(id);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    public Quota getQuota() {
        return quota;
    }

    @Override
    public Optional<Event<?>> nextEvent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextEvent'");
    }

}
