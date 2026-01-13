package com.arqivame.drive.domain.acl;

import java.time.Instant;

import com.arqivame.drive.domain.ValueObject;
import com.arqivame.drive.domain.member.MemberID;

public record Entry(
        MemberID member,
        AccessPermission permission,
        Instant grantedAt) implements ValueObject {

}
