package com.exchanges.connectors.core.ws.handler.orderbook;

import com.exchanges.dto.OrderBook;
import com.exchanges.exception.FeatureNotImplementException;

public interface HandlerOrderBook {
    default OrderBook setSnapshotOrderBook(OrderBook orderBook) {
        throw new FeatureNotImplementException("Set order book snapshot not implemented.");
    }

    default OrderBook update(OrderBook updateOrderBook) {
        throw new FeatureNotImplementException("Updating order book not implemented.");
    }

    default OrderBook cutOrderBook(OrderBook orderBook) {
        throw new FeatureNotImplementException("Cut order book not implemented.");
    }
}
