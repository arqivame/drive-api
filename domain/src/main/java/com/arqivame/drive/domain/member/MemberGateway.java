package com.arqivame.drive.domain.member;

import java.util.Optional;

public interface MemberGateway {

    Optional<Member> findById(MemberID id);

}
