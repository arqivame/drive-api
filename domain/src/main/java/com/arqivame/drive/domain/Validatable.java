package com.arqivame.drive.domain;

import com.arqivame.drive.domain.validation.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler handler);

}
