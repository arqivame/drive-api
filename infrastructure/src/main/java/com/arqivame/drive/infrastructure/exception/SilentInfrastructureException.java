package com.arqivame.drive.infrastructure.exception;

import java.util.List;

public abstract class SilentInfrastructureException extends InfrastructureException {

    private static final boolean VERBOSE = false;

    protected SilentInfrastructureException(final String message) {
        super(message, null, null, VERBOSE);
    }

    protected SilentInfrastructureException(
            final String message,
            final List<InfrastructureException.Error> errors) {
        super(message, errors, null, VERBOSE);
    }

    protected SilentInfrastructureException(
            final String message,
            final List<InfrastructureException.Error> errors,
            final Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
