package com.arqivame.drive.domain.exception;

import java.util.List;

import com.arqivame.drive.domain.user.Quota;

public class QuotaExceededException extends SilentDomainException {

    private QuotaExceededException(final Quota actualQuota) {
        super(
                "Quota exceeded.",
                List.of(DomainException.Error.with("You have exceeded your current quota of "
                        + actualQuota.amount()
                        + " " + actualQuota.unit().name())));
    }

    public static QuotaExceededException with(final Quota actualQuota) {
        return new QuotaExceededException(actualQuota);
    }

}
