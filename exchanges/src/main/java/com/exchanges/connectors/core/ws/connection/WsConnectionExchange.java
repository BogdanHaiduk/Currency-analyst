package com.exchanges.connectors.core.ws.connection;

import com.exchanges.dto.EventConnector;

public interface WsConnectionExchange {
    void connect();

    void ping();

    void subscribe(EventConnector eventConnector);

    void unsubscribe(EventConnector eventConnector);
}
