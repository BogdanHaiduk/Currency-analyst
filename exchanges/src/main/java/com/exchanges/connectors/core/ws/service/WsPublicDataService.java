package com.exchanges.connectors.core.ws.service;

import com.exchanges.dto.CurrencyPair;
import com.exchanges.exception.FeatureNotImplementException;

public interface WsPublicDataService {
    default void subscribeOrderBook(CurrencyPair currencyPair) {
        throw new FeatureNotImplementException("Subscribing on order book not implemented.");
    }

    default void unsubscribeOrderBook(CurrencyPair currencyPair) {
        throw new FeatureNotImplementException("Unsubscribing on order book not implemented.");
    }
}
