package com.exchanges.connectors.core.ws.connection;

import com.exchanges.dto.EventConnector;
import com.exchanges.exception.FeatureNotImplementException;

public interface WsConnectionExchange {
    default void connect() {
        throw new FeatureNotImplementException("WS connection not implemented.");
    }

    default void ping() {
        throw new FeatureNotImplementException("Sending a ping message to the exchange is not implemented");
    }

    default void subscribe(EventConnector eventConnector) {
        throw new FeatureNotImplementException("Exchange update subscription not implemented");
    }

    default void unsubscribe(EventConnector eventConnector) {
        throw new FeatureNotImplementException("Exchange update unsubscription not implemented");
    }
}
