package com.analyst.connection.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "exchangesResponseToAnalyst", durable = "true"),
            exchange = @Exchange(value = "Currency-analyst"),
            key = "exchangesResponseToAnalyst"))
    public void listing(String jsonPayload) {
        //TODO In the next version, you need to implement a message handler
        System.out.println(jsonPayload);
    }
}
