package com.arqivame.drive.domain.event;

import com.arqivame.drive.domain.Entity;

public record EventEntity(String type, String id) {

    public static EventEntity of(final Entity<?> entity) {
        return new EventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}
