package com.exchanges.connectors.connector.ftx.ws.handler;

import com.exchanges.connectors.core.ws.handler.HandlerMessage;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FtxWSHandlerExchange extends WsHandlerExchange {

    @Autowired
    public FtxWSHandlerExchange(RabbitTemplate rabbitTemplate,
                                @Qualifier("ftxHandlerMessage") HandlerMessage handlerMessage) {
        super(rabbitTemplate, handlerMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Successfully established WS connection for FTX.");
    }

    @Override
    public void afterConnectionClosed() {
        log.info("Connection by WS for FTX was lost. All FTX subscriptions canceled.");
    }
}
