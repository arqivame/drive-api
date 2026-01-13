package com.arqivame.drive.domain.member;

import static java.util.Objects.isNull;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.validation.ValidationError;
import com.arqivame.drive.domain.validation.ValidationHandler;

public record Quota(Long amount, QuotaUnit unit) implements ValueObject {

    public static Quota of(Long amount, QuotaUnit unit) {
        return new Quota(amount, unit);
    }

    public long sizeInBytes() {
        return isNull(this.unit) ? 0L : this.unit.toBytes(this.amount);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (isNull(this.amount))
            handler.append(new ValidationError("'amount' should not be null"));

        if (!isNull(this.amount) && this.amount < 0)
            handler.append(new ValidationError("'amount' should be greater than 0"));

        if (isNull(this.unit))
            handler.append(new ValidationError("'unit' should not be null"));
    }

}
