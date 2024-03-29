package com.org.lob.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Value("${rabbitmq.rules_cache.exchange}")
	private String rulesCacheExchange;

	@Bean (name = "rcuFx")
	FanoutExchange fanoutExchange() {
		return new FanoutExchange(rulesCacheExchange);
	}

	@Bean(name = "rcuTriggered")
	Queue flightReceivedQueue() {
		return new AnonymousQueue();
	}

	@Bean
	Binding flightReceivedBinding(@Qualifier("rcuTriggered") Queue queue, @Qualifier("rcuFx") FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}
}
