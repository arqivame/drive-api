package com.arqivame.drive.domain.validation;

import com.arqivame.drive.domain.exception.DomainException;

public record ValidationError(String message) {

    public static ValidationError with(final String message) {
        return new ValidationError(message);
    }

    public static ValidationError fromDomainError(final DomainException.Error domainError) {
        return new ValidationError(domainError.message());
    }

    public DomainException.Error toDomainError() {
        return DomainException.Error.with(message());
    }

}
