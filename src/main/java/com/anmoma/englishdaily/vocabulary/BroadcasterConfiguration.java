package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.RabbitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(RabbitConfiguration.class)
public class BroadcasterConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BroadcasterConfiguration.class);
    @Value("${com.antoniomm.englishdaily.exchange-name}")
    private String exchange;
    @Value("${com.antoniomm.englishdaily.queue-name}")
    private String queue;
    @Value("${com.antoniomm.englishdaily.dead-letter-exchange-name}")
    private String dlxQueue;

    @Bean
    Queue englishDailyDlxQueue() {
        return new Queue(dlxQueue, true, false, false, Collections.emptyMap());
    }

    @Bean
    public Queue englishDailyQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", dlxQueue);
        return new Queue(queue, true, false, false, arguments);
    }

    @Bean
    FanoutExchange englishDailyExchange() {
        return new FanoutExchange(exchange);
    }

    @Bean
    Binding chefBinding(Queue englishDailyQueue, @Qualifier("englishDailyExchange") FanoutExchange chefExchange) {
        return BindingBuilder.bind(englishDailyQueue)
                             .to(chefExchange);
    }

}
