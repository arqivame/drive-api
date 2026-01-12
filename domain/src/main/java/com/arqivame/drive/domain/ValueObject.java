package com.arqivame.drive.domain;

import com.arqivame.drive.domain.validation.ValidationHandler;

public interface ValueObject extends Validatable {

    default void validate(ValidationHandler handler) {
    };

}
