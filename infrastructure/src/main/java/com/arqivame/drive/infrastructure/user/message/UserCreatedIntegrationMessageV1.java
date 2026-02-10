package com.arqivame.drive.infrastructure.user.message;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record UserCreatedIntegrationMessageV1(
        UUID id,
        String email,
        String username,
        String nickname,
        Boolean active,
        Instant createdAt,
        Instant updatedAt) implements Serializable {

}
