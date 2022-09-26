package com.exchanges.connectors.connector.bybit.ws.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BybitChannel {
    ORDER_BOOK("orderbook");

    String channelName;
}
