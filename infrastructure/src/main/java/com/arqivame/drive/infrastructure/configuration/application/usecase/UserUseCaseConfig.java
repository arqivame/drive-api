package com.arqivame.drive.infrastructure.configuration.application.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.application.usecase.user.synchronize.DefaultSynchronizeUserUseCase;
import com.arqivame.drive.application.usecase.user.synchronize.SynchronizeUserUseCase;
import com.arqivame.drive.domain.user.gateway.command.UserCommandGateway;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;

@Configuration
public class UserUseCaseConfig {

    private final UserQueryGateway userQueryGateway;
    private final UserCommandGateway userCommandGateway;

    public UserUseCaseConfig(
            final UserQueryGateway userQueryGateway,
            final UserCommandGateway userCommandGateway) {
        this.userQueryGateway = userQueryGateway;
        this.userCommandGateway = userCommandGateway;
    }

    @Bean
    SynchronizeUserUseCase synchronizeUserUseCase() {
        return new DefaultSynchronizeUserUseCase(userQueryGateway, userCommandGateway);
    }

}
