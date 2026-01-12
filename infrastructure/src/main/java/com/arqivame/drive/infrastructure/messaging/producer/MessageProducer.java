package com.arqivame.drive.infrastructure.messaging.producer;

@FunctionalInterface
public interface MessageProducer<T> {

    void produce(T payload);

}
