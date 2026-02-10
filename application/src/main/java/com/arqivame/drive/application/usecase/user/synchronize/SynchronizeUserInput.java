package com.arqivame.drive.application.usecase.user.synchronize;

import java.util.UUID;

public record SynchronizeUserInput(UUID userId) {

    public static SynchronizeUserInput from(final UUID userId) {
        return new SynchronizeUserInput(userId);
    }

}
