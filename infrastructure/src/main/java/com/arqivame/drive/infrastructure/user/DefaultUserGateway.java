package com.arqivame.drive.infrastructure.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;
import com.arqivame.drive.infrastructure.user.persistence.UserJpaEntity;
import com.arqivame.drive.infrastructure.user.persistence.UserJpaRepository;

@Component
public class DefaultUserGateway implements UserQueryGateway {

    private final UserJpaRepository userJpaRepository;

    public DefaultUserGateway(final UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(final UserID id) {
        return userJpaRepository
                .findById(id.getValue())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public Boolean existsById(UserID id) {
        return userJpaRepository.existsById(id.getValue());
    }

}
