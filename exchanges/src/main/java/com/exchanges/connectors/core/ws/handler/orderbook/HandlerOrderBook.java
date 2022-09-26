package com.exchanges.connectors.core.ws.handler.orderbook;

import com.exchanges.dto.OrderBook;

public interface HandlerOrderBook {
    OrderBook setSnapshotOrderBook(OrderBook orderBook);

    OrderBook update(OrderBook updateOrderBook);

    OrderBook cutOrderBook(OrderBook orderBook);
}
