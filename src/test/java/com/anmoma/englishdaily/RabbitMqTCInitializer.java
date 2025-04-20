package com.anmoma.englishdaily;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public class RabbitMqTCInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static RabbitMQContainer rabbitmq = null;

    static {
        rabbitmq = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.12-management") // or `3.12-alpine` for lighter
        );

        rabbitmq.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of("spring.rabbitmq.host=" + rabbitmq.getHost(), "spring.rabbitmq.port=" + rabbitmq.getAmqpPort(),
                                  "spring.rabbitmq.username=" + rabbitmq.getAdminUsername(), "spring.rabbitmq.password=" + rabbitmq.getAdminPassword())
                          .applyTo(applicationContext.getEnvironment());
    }
}
