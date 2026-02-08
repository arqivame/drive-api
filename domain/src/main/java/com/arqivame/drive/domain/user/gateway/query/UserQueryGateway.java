package com.arqivame.drive.domain.user.gateway.query;

import java.util.Optional;

import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;

public interface UserQueryGateway {

    Optional<User> findById(UserID id);

    Boolean existsById(UserID id);

}
