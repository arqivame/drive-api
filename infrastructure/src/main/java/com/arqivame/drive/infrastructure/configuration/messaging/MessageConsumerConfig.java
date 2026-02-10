package com.arqivame.drive.infrastructure.configuration.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import com.arqivame.drive.application.usecase.user.synchronize.SynchronizeUserUseCase;
import com.arqivame.drive.infrastructure.messaging.consumer.rabbitmq.user.UserCreatedIntegrationConsumerV1;
import com.arqivame.drive.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.drive.infrastructure.user.message.UserCreatedIntegrationMessageV1;

@Configuration
public class MessageConsumerConfig {

    @Bean
    Consumer<Message<UserCreatedIntegrationMessageV1>> userCreatedIntegrationConsumerV1(
            @Value("${application.messaging.consumer.user-created-event.max-attempts}") final Long maxRetryAttempts,
            final MessageProducer<UserCreatedIntegrationMessageV1> errorMessageProducer,
            final SynchronizeUserUseCase synchronizeUserUseCase) {
        return new UserCreatedIntegrationConsumerV1(maxRetryAttempts, errorMessageProducer, synchronizeUserUseCase);
    }

}
