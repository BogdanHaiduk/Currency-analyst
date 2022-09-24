package com.exchanges.connectors.core.ws.service;

public interface WsPublicDataService {
    void subscribeOrderBook(String currencyPair);

    void unsubscribeOrderBook(String currencyPair);
}
