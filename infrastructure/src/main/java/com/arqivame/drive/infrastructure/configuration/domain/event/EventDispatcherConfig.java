package com.arqivame.drive.infrastructure.configuration.domain.event;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.domain.event.EventDispatcher;
import com.arqivame.drive.domain.event.EventHandler;

@Configuration
public class EventDispatcherConfig {

    @Bean
    EventDispatcher eventDispatcher(final List<EventHandler<?>> eventHandlers) {
        final var dispatcher = new EventDispatcher();
        eventHandlers.forEach(handler -> dispatcher.register(handler.eventKey(), handler));
        return dispatcher;
    }

}