package com.arqivame.drive.domain.member;

import java.util.UUID;

import com.arqivame.drive.domain.Identifier;

public class MemberID extends Identifier<UUID> {

    public MemberID(final UUID value) {
        super(value);
    }

    public static MemberID of(final UUID id) {
        return new MemberID(id);
    }

    public static MemberID fromStringValue(final String value) {
        return MemberID.of(UUID.fromString(value));
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "MemberID [value=" + getValue() + "]";
    }

}
