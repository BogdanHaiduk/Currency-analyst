package com.exchanges.connectors.core.ws.handler;

import com.exchanges.connectors.core.ws.handler.orderbook.HandlerOrderBook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class HandlerMessage {
    final HandlerOrderBook handlerOrderBook;

    public HandlerMessage(HandlerOrderBook handlerOrderBook) {
        this.handlerOrderBook = handlerOrderBook;
    }

    public abstract String checkChannel(String jsonPayload);

    public abstract String handleMessageByChannel(String channel, String jsonPayload);
}
