package com.exchanges.connectors.core.ws.handler;

import com.exchanges.dto.Subscribe;
import com.exchanges.exception.ExchangeRuntimeException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class WsHandlerExchange extends TextWebSocketHandler {
    final RabbitTemplate rabbitTemplate;
    final HandlerMessage handlerMessage;
    final Set<Subscribe> subscriptions = new HashSet<>();
    WebSocketSession webSocketSessionConnector;

    @Value("${rabbit.queue.exchanges.response.to.analyst}")
    String exchangesResponseToAnalyst;

    public WsHandlerExchange(RabbitTemplate rabbitTemplate, HandlerMessage handlerMessage) {
        this.rabbitTemplate = rabbitTemplate;
        this.handlerMessage = handlerMessage;
    }

    public abstract void afterConnectionClosed();

    public final Set<Subscribe> getSubscriptions() {
        return subscriptions;
    }

    public final WebSocketSession getWebSocketSessionConnector() {
        return webSocketSessionConnector;
    }

    public final void setWebSocketSessionConnector(WebSocketSession webSocketSessionConnector) {
        if (webSocketSessionConnector != null)
            this.webSocketSessionConnector = webSocketSessionConnector;
        else
            throw new ExchangeRuntimeException("WS session for connector null");
    }

    @Override
    public final void afterConnectionClosed(WebSocketSession webSocketSessionConnectors, CloseStatus status) {
        afterConnectionClosed();
        subscriptions.clear();
    }

    @Override
    public final void handleTextMessage(WebSocketSession webSocketSessionConnectors, TextMessage message) {
        String payload = message.getPayload();
        handlerMessage(payload);
    }

    private void handlerMessage(String payload) {
        String channel = handlerMessage.checkChannel(payload);
        String adaptMessage = handlerMessage.handleMessageByChannel(channel, payload);

        if (adaptMessage != null && !adaptMessage.isEmpty())
            sendMessage(adaptMessage);
    }


    private void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchangesResponseToAnalyst, message);
    }
}
