package com.arqivame.drive.domain.acl;

import java.time.Instant;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.user.UserID;

public record Entry(
        UserID user,
        AccessPermission permission,
        Instant grantedAt) implements ValueObject {

}
