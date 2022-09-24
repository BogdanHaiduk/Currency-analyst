package com.exchanges.connectors.core.ws.service;

import com.exchanges.dto.CurrencyPair;

public interface WsPublicDataService {
    void subscribeOrderBook(CurrencyPair currencyPair);

    void unsubscribeOrderBook(CurrencyPair currencyPair);
}
