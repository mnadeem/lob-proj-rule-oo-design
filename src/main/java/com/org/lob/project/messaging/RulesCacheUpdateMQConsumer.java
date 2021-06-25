package com.org.lob.project.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.lob.project.messaging.model.CacheUpdateEvent;
import com.org.lob.project.service.RuleService;

// https://www.rabbitmq.com/consumers.html
// https://www.rabbitmq.com/tutorials/amqp-concepts.html

@Component
public class RulesCacheUpdateMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RulesCacheUpdateMQConsumer.class);

	private final RuleService ruleService;
	private final ObjectMapper mapper;

	public RulesCacheUpdateMQConsumer(RuleService ruleService, ObjectMapper mapper) {
		this.ruleService = ruleService;
		this.mapper = mapper;
	}

	@RabbitListener(queues = "#{'${rabbitmq.rules_cache_update.triggered.queue}'}")
	public void consumeMessage(String message) {
		try {
			LOGGER.trace("Message received: {} ", message);

			ruleService.reLoad(create(message).getProjectName());

			LOGGER.debug("Message processed successfully with status ");
		} catch (Exception e) {
			LOGGER.error("Unnable to process the Message", e);
		}
	}
	
	private CacheUpdateEvent create(String message) throws JsonProcessingException {
		return this.mapper.readValue(message, CacheUpdateEvent.class);
	}
}
