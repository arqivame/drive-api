package com.arqivame.drive.infrastructure.converter;

import com.arqivame.drive.infrastructure.configuration.mapper.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Caster {

    private static final ObjectMapper mapper = Mapper.mapper();

    public static <T> T cast(Object value, Class<T> targetType) {
        return mapper.convertValue(value, targetType);
    }

}
