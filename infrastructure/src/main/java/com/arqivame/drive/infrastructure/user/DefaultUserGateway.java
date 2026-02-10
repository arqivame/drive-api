package com.arqivame.drive.infrastructure.user;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.arqivame.drive.domain.user.User;
import com.arqivame.drive.domain.user.UserID;
import com.arqivame.drive.domain.user.gateway.command.UserCommandGateway;
import com.arqivame.drive.domain.user.gateway.query.UserQueryGateway;
import com.arqivame.drive.infrastructure.user.persistence.UserJpaEntity;
import com.arqivame.drive.infrastructure.user.persistence.UserJpaRepository;

public class DefaultUserGateway implements UserQueryGateway, UserCommandGateway {

    private final UserJpaRepository userJpaRepository;

    public DefaultUserGateway(final UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional
    @Override
    public User create(final User user) {

        if (userJpaRepository.existsById(user.getId().getValue()))
            throw new RuntimeException("User with id " + user.getId() + " already exists");

        userJpaRepository.save(UserJpaEntity.fromDomain(user));

        return user;
    }

    @Override
    public Optional<User> findById(final UserID id) {
        return userJpaRepository
                .findById(id.getValue())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public Boolean existsById(final UserID id) {
        return userJpaRepository.existsById(id.getValue());
    }

}
