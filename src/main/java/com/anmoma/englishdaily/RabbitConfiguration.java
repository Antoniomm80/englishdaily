package com.anmoma.englishdaily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.exception.FatalListenerExecutionException;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
@EnableRabbit
public class RabbitConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RabbitConfiguration.class);

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.vhost}")
    private String vhost;

    @Value("${rabbitmq.receiver.retry.initial-interval}")
    private int initialInterval;
    @Value("${rabbitmq.receiver.retry.max-attempts}")
    private int maxAttemps;
    @Value("${rabbitmq.receiver.retry.multiplier}")
    private int multiplier;
    @Value("${rabbitmq.receiver.retry.max-interval}")
    private int maxInterval;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        log.info("Configuring RabbitMQ");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(reservationRetryInterceptor());
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor reservationRetryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                                      .maxAttempts(maxAttemps)
                                      .backOffOptions(initialInterval, multiplier, maxInterval)
                                      .recoverer(recoverer())
                                      .build();
    }

    private MessageRecoverer recoverer() {
        return (message, cause) -> {
            throw new FatalListenerExecutionException("Retry Policy Exhausted", new AmqpRejectAndDontRequeueException(cause));
        };
    }

}
