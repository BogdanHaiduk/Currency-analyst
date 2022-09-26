package com.exchanges.connectors.connector.bybit.ws.handler;

import com.exchanges.connectors.core.ws.handler.HandlerMessage;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class BybitWsHandlerExchange extends WsHandlerExchange {
    public BybitWsHandlerExchange(RabbitTemplate rabbitTemplate,
                                  @Qualifier("bybitWSHandlerMessage") HandlerMessage handlerMessage) {
        super(rabbitTemplate, handlerMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Successfully established WS connection for Bybit.");
    }

    @Override
    public void afterConnectionClosed() {
        log.info("Connection by WS for Bybit was lost. All Bybit subscriptions canceled.");
    }
}
