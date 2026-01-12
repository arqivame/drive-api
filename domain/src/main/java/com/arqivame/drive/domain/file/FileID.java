package com.arqivame.drive.domain.file;

import java.util.UUID;

import com.arqivame.drive.domain.Identifier;

public class FileID extends Identifier<UUID> {

    private FileID(final UUID id) {
        super(id);
    }

    public static FileID of(final UUID id) {
        return new FileID(id);
    }

    public static FileID unique() {
        return FileID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "FileID [value=" + getValue() + "]";
    }

}
