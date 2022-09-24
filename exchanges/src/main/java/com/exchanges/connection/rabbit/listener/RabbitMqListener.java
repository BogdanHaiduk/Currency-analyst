package com.exchanges.connection.rabbit.listener;

import com.exchanges.dto.MessageEvent;
import com.exchanges.connection.rabbit.service.CommandService;
import com.exchanges.dto.ExceptionMessage;
import com.exchanges.exception.ExchangeRuntimeException;
import com.exchanges.util.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMqListener {
    final CommandService commandService;
    final RabbitTemplate rabbitTemplate;

    @Value("${rabbit.queue.exchanges.response.to.analyst}")
    String exchangesResponseToAnalyst;

    @Autowired
    public RabbitMqListener(@Qualifier("commandServiceImpl") CommandService commandService, RabbitTemplate rabbitTemplate) {
        this.commandService = commandService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "analystRequestToExchanges", durable = "true"),
            exchange = @Exchange(value = "Currency-analyst"),
            key = "analystRequestToExchanges"))
    public void listenerAnalystMS(String message) {
        try {
            MessageEvent messageEvent = JsonUtils.fromJson(message, MessageEvent.class);
            commandService.handleMessage(messageEvent);
        } catch (ExchangeRuntimeException ex) {
            log.error("Unable to handle message, problem - {}", ex.getMessage(), ex);
            rabbitTemplate.convertAndSend(exchangesResponseToAnalyst, JsonUtils.toJson(new ExceptionMessage(ex.getMessage())));
        }
    }
}
