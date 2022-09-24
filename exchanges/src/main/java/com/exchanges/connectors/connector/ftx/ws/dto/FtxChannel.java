package com.exchanges.connectors.connector.ftx.ws.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum FtxChannel {
    ORDER_BOOK("orderbook");

    String name;
}
