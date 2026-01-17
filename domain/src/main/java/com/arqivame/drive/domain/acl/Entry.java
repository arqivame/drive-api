package com.arqivame.drive.domain.acl;

import static java.util.Objects.isNull;

import java.time.Instant;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.user.UserID;

public record Entry(
        UserID user,
        AccessPermission permission,
        Instant grantedAt) implements ValueObject {

    public static Entry create(UserID user, AccessPermission permission) {
        return new Entry(user, permission, Instant.now());
    }

    public Boolean isEquivalentTo(final Entry other) {

        return isNull(other) ? false
                : user().equals(other.user()) && permission().equals(other.permission());

    }

}
