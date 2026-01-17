package com.arqivame.drive.infrastructure.exception;

import java.util.ArrayList;
import java.util.List;

public class InfrastructureException extends RuntimeException {

    private final List<InfrastructureException.Error> errors;

    protected InfrastructureException(
            final String message,
            final List<InfrastructureException.Error> errors,
            final Throwable cause,
            final boolean verbose) {
        super(message, cause, enableSuppression(verbose), writableStackTrace(verbose));
        this.errors = addCauseToErrors(errors, cause) == null ? List.of() : new ArrayList<>(errors);
    }

    public List<InfrastructureException.Error> getErrors() {
        return List.copyOf(errors);
    }

    public String errorsToString() {
        return "[" + errors.stream()
                .map(InfrastructureException.Error::message)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No errors") + "]";
    }

    private static boolean enableSuppression(final boolean verbose) {
        return verbose ? false : true;
    }

    private static boolean writableStackTrace(final boolean verbose) {
        return verbose ? true : false;
    }

    private static List<InfrastructureException.Error> addCauseToErrors(
            final List<InfrastructureException.Error> errors,
            final Throwable cause) {
        if (cause == null)
            return errors;

        final List<InfrastructureException.Error> result = new ArrayList<>(errors != null ? errors : List.of());
        result.add(InfrastructureException.Error.with(cause));
        return result;
    }

    public record Error(String message) {

        public static InfrastructureException.Error with(final String message) {
            return new InfrastructureException.Error(message);
        }

        public static InfrastructureException.Error with(final Throwable cause) {
            return new InfrastructureException.Error(
                    "Exception:[" + cause.getClass().getName() + "] Message:[" + cause.getMessage() + "]");
        }

    }
}
