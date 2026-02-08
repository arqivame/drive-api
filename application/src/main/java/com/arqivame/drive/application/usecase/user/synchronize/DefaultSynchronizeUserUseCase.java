package com.arqivame.drive.application.usecase.user.synchronize;

import static java.util.Objects.requireNonNull;

import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.user.gateway.command.UserCommandGateway;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;

public class DefaultSynchronizeUserUseCase extends SynchronizeUserUseCase {

    private final UserQueryGateway userQueryGateway;
    private final UserCommandGateway userCommandGateway;

    public DefaultSynchronizeUserUseCase(
            final UserQueryGateway userQueryGateway,
            final UserCommandGateway userCommandGateway) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.userCommandGateway = requireNonNull(userCommandGateway);
    }

    @Override
    public void execute(final SynchronizeUserInput input) {

        final UserID userId = UserID.of(input.userId());

        if (userQueryGateway.existsById(userId))
            return;

        userCommandGateway.create(User.create(userId));

    }

}
