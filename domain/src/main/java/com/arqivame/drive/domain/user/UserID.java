package com.arqivame.drive.domain.user;

import java.util.UUID;

import com.arqivame.drive.domain.Identifier;

public class UserID extends Identifier<UUID> {

    public UserID(final UUID value) {
        super(value);
    }

    public static UserID of(final UUID id) {
        return new UserID(id);
    }

    public static UserID fromStringValue(final String value) {
        return UserID.of(UUID.fromString(value));
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "UserID [value=" + getValue() + "]";
    }

}
