package com.arqivame.drive.infrastructure.configuration.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.drive.infrastructure.messaging.producer.MessageProducer;
import com.arqivame.drive.infrastructure.messaging.producer.springcloud.SpringCloudMessageProducer;
import com.arqivame.drive.infrastructure.user.message.UserCreatedIntegrationMessageV1;

@Configuration
public class MessageProducerConfig {

    @Bean
    MessageProducer<UserCreatedIntegrationMessageV1> userCreatedIntegrationErrorMessageProducer(
            final StreamBridge streamBridge) {
        return new SpringCloudMessageProducer<>(streamBridge, "userCreatedIntegrationEventV1Error-out-0");
    }

}
