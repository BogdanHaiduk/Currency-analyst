package com.exchanges.connectors.core;

import com.exchanges.connectors.core.rest.service.RestPublicDataService;
import com.exchanges.connectors.core.ws.service.WsPublicDataService;
import com.exchanges.connectors.core.ws.connection.WsConnectionExchange;
import com.exchanges.connectors.core.ws.handler.WsHandlerExchange;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.socket.WebSocketSession;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Exchange {
    final WsHandlerExchange wsHandlerExchange;

    public Exchange(WsHandlerExchange wsHandlerExchange) {
        this.wsHandlerExchange = wsHandlerExchange;
    }

    public boolean checkWorkSession() {
        WebSocketSession webSocketSessionConnector = wsHandlerExchange.getWebSocketSessionConnector();

        return webSocketSessionConnector != null && webSocketSessionConnector.isOpen();
    }

    public abstract String getExchangeName();

    public abstract WsConnectionExchange getWsConnectionExchange();

    public abstract WsPublicDataService getWSPublicDataService();

    public abstract RestPublicDataService getRestPublicDataService();
}