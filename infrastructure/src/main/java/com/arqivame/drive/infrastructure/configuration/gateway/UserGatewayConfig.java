package com.arqivame.drive.infrastructure.configuration.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.infrastructure.user.DefaultUserGateway;
import com.arqivame.drive.infrastructure.user.persistence.UserJpaRepository;

@Configuration
public class UserGatewayConfig {

    private final UserJpaRepository userJpaRepository;

    public UserGatewayConfig(final UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Bean
    DefaultUserGateway defaultUserGateway() {
        return new DefaultUserGateway(userJpaRepository);
    }

}