package com.arqivame.drive.infrastructure.messaging.consumer.rabbitmq.user;

import java.util.Set;

import org.springframework.messaging.Message;

import com.arqivame.drive.application.usecase.user.synchronize.SynchronizeUserInput;
import com.arqivame.drive.application.usecase.user.synchronize.SynchronizeUserUseCase;
import com.arqivame.drive.infrastructure.messaging.consumer.rabbitmq.RabbitMQMessageConsumer;
import com.arqivame.drive.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.drive.infrastructure.user.message.UserCreatedIntegrationMessageV1;

public class UserCreatedIntegrationConsumerV1 extends RabbitMQMessageConsumer<UserCreatedIntegrationMessageV1> {

    private final SynchronizeUserUseCase synchronizeUserUseCase;

    public UserCreatedIntegrationConsumerV1(
            final Long maxRetryAttempts,
            final MessageProducer<UserCreatedIntegrationMessageV1> errorMessageProducer,
            final SynchronizeUserUseCase synchronizeUserUseCase) {
        super(maxRetryAttempts, errorMessageProducer, Set.of());
        this.synchronizeUserUseCase = synchronizeUserUseCase;
    }

    @Override
    public void consume(final Message<UserCreatedIntegrationMessageV1> message) {

        synchronizeUserUseCase.execute(SynchronizeUserInput.from(message.getPayload().id()));

    }

}
