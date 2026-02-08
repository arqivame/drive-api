package com.arqivame.drive.domain.user;

import java.time.Instant;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;

public record QuotaRequest(Quota requestedQuota, Instant requesteddAt) implements ValueObject {

    public static QuotaRequest create(Quota requestedQuota) {
        return of(requestedQuota, Instant.now());
    }

    public static QuotaRequest of(Quota requestedQuota, Instant requestedAt) {
        return new QuotaRequest(requestedQuota, requestedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (requestedQuota == null)
            handler.append(new ValidationError("'requestedQuota' should not be null"));

        if (requesteddAt == null)
            handler.append(new ValidationError("'requestedAt' should not be null"));

        if (requestedQuota != null)
            requestedQuota.validate(handler);
    }

}
