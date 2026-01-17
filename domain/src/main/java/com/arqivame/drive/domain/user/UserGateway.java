package com.arqivame.drive.domain.user;

import java.util.Optional;

public interface UserGateway {

    Optional<User> findById(UserID id);

    Boolean existsById(UserID id);

}
