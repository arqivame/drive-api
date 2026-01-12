package com.arqivame.drive.infrastructure.event.presenter;

import com.arqivame.drive.domain.event.Event;
import com.arqivame.drive.domain.event.EventEntity;
import com.arqivame.drive.infrastructure.event.model.EventMessage;

public interface EventPresenter {

    public static EventMessage.Metadata present(final Event<?> event) {
        return new EventMessage.Metadata(
                event.getMetadata().domain(),
                event.getMetadata().entity(),
                event.getMetadata().action(),
                event.getMetadata().service(),
                event.getMetadata().version(),
                event.getMetadata().occurredAt(),
                event.getMetadata().relatedEntities()
                        .stream()
                        .map(EventPresenter::present)
                        .collect(java.util.stream.Collectors.toSet()));
    }

    private static EventMessage.EventEntity present(final EventEntity eventEntity) {
        return new EventMessage.EventEntity(eventEntity.type(), eventEntity.id());
    }

}
