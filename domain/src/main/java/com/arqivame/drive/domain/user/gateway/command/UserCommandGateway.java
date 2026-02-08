package com.arqivame.drive.domain.user.gateway.command;

import com.arqivame.drive.domain.user.User;

public interface UserCommandGateway {

    User create(User user);

}
