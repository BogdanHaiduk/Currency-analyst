package com.exchanges.connectors.core.ws.connection;

import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import com.exchanges.dto.CurrencyPair;
import com.exchanges.dto.EventConnector;
import com.exchanges.dto.Subscribe;
import com.exchanges.dto.Topic;
import com.exchanges.exception.ExchangeRuntimeException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class WsConnectionExchangeImpl implements WsConnectionExchange {
    final WsHandlerExchange wsHandlerExchange;

    public WsConnectionExchangeImpl(WsHandlerExchange wsHandlerExchange) {
        this.wsHandlerExchange = wsHandlerExchange;
    }

    public abstract String getPathWS();

    protected abstract int getTimePing();

    protected abstract TimeUnit getUnitTimePing();

    protected abstract String getMessagePing();

    @Override
    public void connect() {
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            WebSocketSession webSocketSessionConnector = webSocketClient.doHandshake(
                            wsHandlerExchange,
                            new WebSocketHttpHeaders(),
                            URI.create(getPathWS()))
                    .get();

            wsHandlerExchange.setWebSocketSessionConnector(webSocketSessionConnector);
            ping();
        } catch (Exception e) {
            String message = String.format("Connection to the connector by %s was not successful", getPathWS());
            log.error("{}, problem - {}", message, e.getMessage(), e);

            throw new ExchangeRuntimeException("Connection to the connector was not successful");
        }
    }

    @Override
    public void ping() {
        ScheduledExecutorService statusScheduledThread = Executors.newSingleThreadScheduledExecutor();

        statusScheduledThread.scheduleAtFixedRate(() -> {
            try {
                wsHandlerExchange.getWebSocketSessionConnector()
                        .sendMessage(
                                new TextMessage(getMessagePing())
                        );
            } catch (IOException e) {
                log.error("Sending a ping message by {} was not successful, problem - {}",
                        getPathWS(),
                        e.getMessage(),
                        e);
            }
        }, 1, getTimePing(), getUnitTimePing());
    }

    @Override
    public void subscribe(EventConnector eventConnector) {
        addSubscribe(eventConnector.getCurrencyPair(), eventConnector.getTopic());

        if (eventConnector.getJsonMessageForExchange() != null) {
            try {
                wsHandlerExchange.getWebSocketSessionConnector()
                        .sendMessage(
                                new TextMessage(eventConnector.getJsonMessageForExchange())
                        );
            } catch (IOException e) {
                String message = String.format("Sending a subscribe message by %s was not successful", getPathWS());
                log.error("{}, problem - {}", message, e.getMessage(), e);

                throw new ExchangeRuntimeException(message);
            }
        }
    }

    private void addSubscribe(CurrencyPair currencyPair, Topic topic) {
        wsHandlerExchange.getSubscribes()
                .add(
                        new Subscribe(currencyPair, topic)
                );
    }

    @Override
    public void unsubscribe(EventConnector eventConnector) {
        removeSubscribe(eventConnector.getCurrencyPair(), eventConnector.getTopic());

        if (eventConnector.getJsonMessageForExchange() != null) {
            try {
                wsHandlerExchange.getWebSocketSessionConnector()
                        .sendMessage(
                                new TextMessage(eventConnector.getJsonMessageForExchange())
                        );
            } catch (IOException e) {
                String message = String.format("Sending a unsubscribe message by %s was not successful", getPathWS());
                log.error("{}, problem - {}", message, e.getMessage(), e);

                throw new ExchangeRuntimeException(message);
            }
        }
    }

    private void removeSubscribe(CurrencyPair currencyPair, Topic topic) {
        wsHandlerExchange.getSubscribes().remove(
                new Subscribe(currencyPair, topic)
        );
    }
}
