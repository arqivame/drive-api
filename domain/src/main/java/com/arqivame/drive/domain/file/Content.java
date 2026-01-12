package com.arqivame.drive.domain.file;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;

public record Content(String type, long size) implements ValueObject {

    public static Content of(final String type, final long size) {
        return new Content(type, size);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        validateType(handler);
        validateSize(handler);
    }

    private void validateType(final ValidationHandler handler) {
        if (type == null)
            handler.append(new ValidationError("'type' cannot be null."));
        else if (type.trim().isEmpty())
            handler.append(new ValidationError("'type' cannot be empty."));

    }

    private void validateSize(final ValidationHandler handler) {
        if (size < 0)
            handler.append(new ValidationError("'size' cannot be negative."));
    }

}
