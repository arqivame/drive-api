package com.arqivame.drive.domain.folder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;

public record FolderName(String value) implements ValueObject {

    private static final String INVALID_CHARACTERS_REGEX = "[./\\\\:*?\"<>|]";
    private static final Pattern INVALID_CHARACTERS_PATTERN = Pattern.compile(INVALID_CHARACTERS_REGEX);

    public static FolderName of(final String value) {
        return new FolderName(value);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (value == null) {
            handler.append(ValidationError.with("'value' cannot be null"));
            return;
        }

        if (value.trim().isEmpty()) {
            handler.append(new ValidationError("'value' cannot be empty."));
            return;
        }

        final String trimmedName = value.trim();
        if (trimmedName.length() < 1 || trimmedName.length() > 64)
            handler.append(ValidationError
                    .with("'name' must be between " + 1 + " and " + 64 + " characters."));

        if (containsInvalidCharacters(value))
            handler.append(new ValidationError("'name' contains invalid characters."));

    }

    private static boolean containsInvalidCharacters(String input) {
        Matcher matcher = INVALID_CHARACTERS_PATTERN.matcher(input);
        return matcher.find();
    }

}
